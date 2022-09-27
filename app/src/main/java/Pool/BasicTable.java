package Pool;

import javafx.scene.paint.Paint;

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

    public List<Ball> getBalls() {
        return balls;
    }

    public BasicTable(Long sizeX, Long sizeY, double friction, String col){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.friction = friction;
        this.col = col;
        this.colour = Paint.valueOf(col);
        balls = parseBall();
        for (Ball ball: this.balls) {
            ((colBall) ball).setRadius(Math.sqrt(this.sizeX * this.sizeY / ratio));
            if (ball.getX() + ball.getRadius() > this.sizeX) ball.setX(this.sizeX - ball.getRadius());
            if (ball.getX() - ball.getRadius() < 0) ball.setX(0 + ball.getRadius());
            if (ball.getY() + ball.getRadius() > this.sizeY) ball.setY(this.sizeY - ball.getRadius());
            if (ball.getY() - ball.getRadius() < 0) ball.setY(0 + ball.getRadius());
        }
    }

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



    public static void main(String[] args) {
    }
}
