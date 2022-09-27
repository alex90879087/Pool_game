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



    public static void main(String[] args) {
    }
}
