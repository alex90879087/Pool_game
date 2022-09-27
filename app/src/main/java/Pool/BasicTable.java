package Pool;

import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BasicTable implements Table {

    private Paint colour;
    private double friction;
    private double sizeX;

    private double sizeY;
    private String col;

    public BasicTable(double sizeX, double sizeY, double friction, String col){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.friction = friction;
        this.col = col;
        this.colour = Paint.valueOf(col);
    }

    public double getFriction() { return this.friction;}

    public double getX() {
        return sizeX;
    }

    public double getY() {
        return sizeY;
    }

    public String getCol() {
        return col;
    }

}
