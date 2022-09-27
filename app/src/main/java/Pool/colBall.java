package Pool;

import javafx.scene.paint.Paint;

import java.util.Locale;

public class colBall implements Ball {
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double mass;
    private double radius = 12;
    private Paint colour;
    private String col;


    public colBall (double xPos, double yPos, double xVel, double yVel, double mass, String colour) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.mass = mass;
        this.colour = Paint.valueOf(colour.toUpperCase(Locale.ROOT));
        this.col = colour;
    }


    @Override
    public double getX() {return this.xPos;}

    @Override
    public double getY() {return this.yPos;}

    @Override
    public String getCol() {return this.col;}

    public double getxVel() {return xVel;}

    public double getyVel() {return yVel;}

    public double getMass() {return mass;}

    public Paint getColour() {return this.colour;}

    public double getRadius() {return radius;}

    public void setRadius(double raidus) {this.radius = radius;}
}
