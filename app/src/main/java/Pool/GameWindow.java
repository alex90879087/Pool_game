package Pool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.security.Key;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static Pool.Reader.parseBall;

public class GameWindow {

    private final GraphicsContext gc;
    private Scene scene;
    private BasicTable model;
    private Pane pane;
    private  double height;
    private  double  width;
    private Line currentLine;
    private List<Ball> balls;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // to record the start position when click on the cue ball
    private double startX;
    private double startY;

    private colBall cueBall;
    private List<Circle> pockets = new ArrayList<>();

    public List<Ball> getBalls() { return balls;}
    public void setBalls(List<Ball> balls) {this.balls = balls;}

    GameWindow(BasicTable table){
        this.model =  table;
        this.pane = new Pane();
        height = table.getY();
        width = table.getX();
        this.scene = new Scene(this.pane, width, height);
        Canvas canvas = new Canvas(width,  height);
        gc = canvas.getGraphicsContext2D();
        this.cueBall = (colBall) this.model.getCueBall();
        this.balls = model.getBalls();
        this.pockets = model.getPockets();
        this.status = true;

        // pane is the lowest level in the hierarchy in this case
        pane.getChildren().add(canvas);
    }

    void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                a -> {


                    this.draw();

//                  can only click cue ball when it is not moving
                    pane.setOnMousePressed(e -> {
                        if (cueBall.getMoving() == false && cursorCueBall(e.getX(), e.getY())) {
                            currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                            pane.getChildren().add(currentLine);

                            pane.setOnMouseDragged(d -> {
                                if (cueBall.getMoving() == false) {
                                    if (currentLine == null) {
                                        currentLine = new Line();
                                        currentLine.setStartX(startX);
                                        currentLine.setStartY(startY);
                                    }
                                    else {
                                        currentLine.setEndX(d.getX());
                                        currentLine.setEndY(d.getY());
                                    }
                                }
                            });
                            pane.setOnMouseReleased(q -> {
                                if (cueBall.getMoving() == false) {
                                    double distance = Math.hypot(startX - q.getX(), startY - q.getY());
                                    shoot(distance);
                                    pane.getChildren().remove(currentLine);
                                }
                            });
                        }
                    });




                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        if (!status) {
            System.out.println("Game over");
            timeline.stop();
        }
    }

    void moves() {
        for (Ball ball: balls) {
            if (ball.getMoving()) ball.move(model.getFriction());
        }
    }

    // distance of start and end point is used to calculate the power of the shot
    public void shoot(double distance) {

        // power of the shot
        double power = (distance < 250) ? 3.5 : (distance < 450) ? 4 : 4.5;

        // direction of the mouse dragging (complex number vector)
        this.cueBall.setyVel((-(currentLine.getEndY() - currentLine.getStartY())) / 5 * power);
        this.cueBall.setxVel((-(currentLine.getEndX() - currentLine.getStartX())) / 5 * power);

    }


    private void draw() {

        inPocket();
        tick();
        SlowDown(this.model.getFriction());
        moves();

        gc.clearRect(0,0, width, height);
        gc.setFill(model.getColour());
        gc.fillRect(0,0, model.getX(),model.getY());


        for (Circle c: this.pockets) {
            gc.setFill(Paint.valueOf("BLACK"));
            gc.fillOval(c.getCenterX() - c.getRadius(),
                    c.getCenterY() - c.getRadius(),
                    c.getRadius() * 2,
                    c.getRadius() * 2);
        }


        for (Ball ball: model.getBalls()){
            gc.setFill(((colBall) ball).getColour());
            gc.fillOval(ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    ball.getRadius() * 2,
                    ball.getRadius() * 2);
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    void tick() {

        for(Ball ball: balls) {

            if (ball.getX() + ball.getRadius() > width) {
                ball.setX(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -0.7);
            }
            if (ball.getX() - ball.getRadius() < 0) {
                ball.setX(0 + ball.getRadius());
                ball.setxVel(ball.getxVel() *  -0.7);
            }
            if (ball.getY() + ball.getRadius() > height) {
                ball.setY(height - ball.getRadius());
                ball.setyVel(ball.getyVel() *  -0.7);
            }
            if (ball.getY() - ball.getRadius() < 0) {
                ball.setY(0 + ball.getRadius());
                ball.setyVel(ball.getyVel() *  -0.7);
            }

            for(Ball ballB: balls) {
                if (checkCollision(ball, ballB)) {
                    handleCollision(ball, ballB);
                }
            }
        }


    }

    void SlowDown(double friction) {

        for (Ball ball: balls) {
            if (ball.getMoving()) {

                double coeX;
                double coeY;
                coeX = ball.getxVel() / 5;

                // slow down x
                if (ball.getxVel() > 0) {
                    ball.setxVel(ball.getxVel() - friction / 10);
                }
                if (ball.getxVel() < 0) {
                    ball.setxVel(ball.getxVel() + friction / 10);
                }
                if (ball.getyVel() > 0) {
                    ball.setyVel(ball.getyVel() - friction / 10);
                }
                if (ball.getyVel() < 0) {
                    ball.setyVel(ball.getyVel() + friction / 10);
                }

                if (Math.abs(ball.getxVel()) <= 0.1) ball.setxVel(0);
                if (Math.abs(ball.getyVel()) <= 0.1) ball.setyVel(0);

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

    void inPocket() {

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

    boolean cursorCueBall(double x, double y) {
        return (x < cueBall.getX() + cueBall.getRadius() &&
                x > cueBall.getX() - cueBall.getRadius()) &&
               (y < cueBall.getY() + cueBall.getRadius() &&
                y > cueBall.getY() - cueBall.getRadius());
    }

    void reset() {
        balls = parseBall();
    }


}
