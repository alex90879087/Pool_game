package Pool;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static Pool.Reader.parseBall;
import static Pool.Reader.parseTable;

public class BasicTable implements Table {

    private Paint colour;
    private String col;

    // ratio of ball and table -> to decide radius
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


    public BasicTable(Long sizeX, Long sizeY, double friction, String col){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.friction = friction;
        this.col = col;
        this.colour = Paint.valueOf(col);
        balls = parseBall();
        for (Ball ball: this.balls) {
            if (ball.getCol().equalsIgnoreCase("white")) cueBall = ball;
            ((colBall) ball).setRadius(Math.sqrt(this.sizeX * this.sizeY / ratio));
            if (ball.getX() + ball.getRadius() > this.sizeX) ball.setX(this.sizeX - ball.getRadius());
            if (ball.getX() - ball.getRadius() < 0) ball.setX(0 + ball.getRadius());
            if (ball.getY() + ball.getRadius() > this.sizeY) ball.setY(this.sizeY - ball.getRadius());
            if (ball.getY() - ball.getRadius() < 0) ball.setY(0 + ball.getRadius());
        }
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




}
