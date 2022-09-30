package Pool;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.*;

import static Pool.Reader.parseBall;
import static Pool.Reader.parseTable;

public class BasicTable implements Table {

    private Paint colour;
    private String col;

    // ratio of ball and table -> to decide radius of balls
    public  double ratio = (262 * 150) / (Math.pow(5.715, 2) * Math.PI) ;
    private double friction;
    private Long sizeX;
    private Long sizeY;
    private List<Ball> balls;
    private Ball cueBall;

    public List<Ball> getBalls() {
        return balls;
    }
    public Ball getCueBall() {return this.cueBall;}

    private List<Circle> pockets = new ArrayList<>();
    private List<Ball> originalBalls;
    private boolean playing;

    public void findCueBall() {
        for (Ball ball: balls) { if (ball.getCol().equalsIgnoreCase("white")) cueBall = ball;}
    }

    public BasicTable(Long sizeX, Long sizeY, double friction, String col){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.friction = friction;
        this.col = col;
        this.colour = Paint.valueOf(col);


        setBallsAndCheck();

        Circle p1 = new Circle(0,0,40);
        Circle p2 = new Circle(0,sizeY,40);
        Circle p3 = new Circle(sizeX,sizeY,40);
        Circle p4 = new Circle(sizeX,0,40);
        Circle p5 = new Circle(sizeX / 2,0,30);
        Circle p6 = new Circle(sizeX / 2,sizeY,30);
        pockets.add(p1);
        pockets.add(p2);
        pockets.add(p3);
        pockets.add(p4);
        pockets.add(p5);
        pockets.add(p6);
        playing = true;
    }

    public void setBallsAndCheck() {

        this.balls = parseBall();

        for (Ball ball: this.balls) {
            findCueBall();
            ((ColBall) ball).setRadius(Math.sqrt(this.sizeX * this.sizeY / ratio));
            if (ball.getX() + ball.getRadius() > this.sizeX) ball.setX(this.sizeX - ball.getRadius());
            if (ball.getX() - ball.getRadius() < 0) ball.setX(0 + ball.getRadius());
            if (ball.getY() + ball.getRadius() > this.sizeY) ball.setY(this.sizeY - ball.getRadius());
            if (ball.getY() - ball.getRadius() < 0) ball.setY(0 + ball.getRadius());
        }
        originalBalls = parseBall();
    }

    public List<Circle> getPockets() {return pockets;}

    public double getFriction() { return this.friction;}

    public Long getX() {
        return sizeX;
    }

    public Long getY() {
        return sizeY;
    }

    public String getCol() {
        return col;
    }

    public Paint getColour() {return colour;}

    public void moves() {
        for (Ball ball: balls) {
            if (ball.getMoving()) ball.move(this.getFriction());
        }
    }

    public void inPocket() {

        try{
            for (Ball ball: balls) {
                if (ball.getMoving()) {
                    for (Circle pocket: pockets) {
                        if (pocket.contains(ball.getX(), ball.getY())) {
                            ball.executeStrat(this);
                            if (!ball.exist()) balls.remove(ball);
                        }
                    }
                }
            }
        }catch(ConcurrentModificationException e){}

    }

    public void setPlaying(boolean status) {
        this.playing = status;
    }
    public boolean getPlaying() {return this.playing;}

    public void reset(){
        this.balls = parseBall();
        System.out.println(balls.size());
        findCueBall();
    }

    public List<Ball> getOriginalBalls() {return this.originalBalls;}

    public void SlowDown(double friction) {

        for (Ball ball: balls) {
            if (ball.getMoving()) {
                // slow down x
                if (ball.getxVel() > 0) ball.setxVel(ball.getxVel() - friction / 10);


                if (ball.getxVel() < 0) ball.setxVel(ball.getxVel() + friction / 10);


                if (ball.getyVel() > 0) ball.setyVel(ball.getyVel() - friction / 10);


                if (ball.getyVel() < 0) ball.setyVel(ball.getyVel() + friction / 10);


                if (Math.abs(ball.getxVel()) <= 0.1) ball.setxVel(0);

                if (Math.abs(ball.getyVel()) <= 0.1) ball.setyVel(0);
            }
        }
    }
    public void tick() {
        for(Ball ball: this.getBalls()) {

            if (ball.getX() + ball.getRadius() > this.sizeX) {
                ball.setX(sizeX - ball.getRadius());
                ball.setxVel(ball.getxVel() * -0.7);
            }
            if (ball.getX() - ball.getRadius() < 0) {
                ball.setX(0 + ball.getRadius());
                ball.setxVel(ball.getxVel() *  -0.7);
            }
            if (ball.getY() + ball.getRadius() > sizeY) {
                ball.setY(sizeY - ball.getRadius());
                ball.setyVel(ball.getyVel() *  -0.7);
            }
            if (ball.getY() - ball.getRadius() < 0) {
                ball.setY(0 + ball.getRadius());
                ball.setyVel(ball.getyVel() *  -0.7);
            }

            for(Ball ballB: this.getBalls()) {
                if (checkCollision(ball, ballB)) {
                    handleCollision(ball, ballB);
                }
            }
        }
    }
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getX() - ballB.getX()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getY() - ballB.getY()) < ballA.getRadius() + ballB.getRadius();
    }
    private void handleCollision(Ball ballA, Ball ballB) {

        //Properties of two colliding balls
        Point2D posA = new Point2D(ballA.getX(), ballA.getY());
        Point2D posB = new Point2D(ballB.getX(), ballB.getY());
        Point2D velA = new Point2D(ballA.getxVel(), ballA.getyVel());
        Point2D velB = new Point2D(ballB.getxVel(), ballB.getyVel());

        //calculate the axis of collision
        Point2D collisionVector = posB.subtract(posA);
        collisionVector = collisionVector.normalize();

        //the proportion of each balls velocity along the axis of collision
        double vA = collisionVector.dotProduct(velA);
        double vB = collisionVector.dotProduct(velB);

        //if balls are moving away from each other do nothing
        if (vA <= 0 && vB >= 0) {
            return;
        }

        // We're working with equal mass balls today
        //double mR = massB/massA;
        double mR = 1;

        //The velocity of each ball after a collision can be found by solving the quadratic equation
        //given by equating momentum energy and energy before and after the collision and finding the
        //velocities that satisfy this
        //-(mR+1)x^2 2*(mR*vB+vA)x -((mR-1)*vB^2+2*vA*vB)=0
        //first we find the discriminant
        double a = -(mR + 1);
        double b = 2 * (mR * vB + vA);
        double c = -((mR - 1) * vB * vB + 2 * vA * vB);
        double discriminant = Math.sqrt(b * b - 4 * a * c);
        double root = (-b + discriminant)/(2 * a);

        //only one of the roots is the solution, the other pertains to the current velocities
        if (root - vB < 0.01) {
            root = (-b - discriminant)/(2 * a);
        }

        //The resulting changes in velocity for ball A and B
        Point2D deltaVA = collisionVector.multiply(mR * (vB - root));
        Point2D deltaVB = collisionVector.multiply(root - vB);

        ballA.setxVel(ballA.getxVel() + deltaVA.getX());
        ballA.setyVel(ballA.getyVel() + deltaVA.getY());
        ballB.setxVel(ballB.getxVel() + deltaVB.getX());
        ballB.setyVel(ballB.getyVel() + deltaVB.getY());
    }

}
