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
    private boolean moving;
    private Strategy strat;


    public colBall (double xPos, double yPos, double xVel, double yVel, double mass, String colour) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.mass = mass;
        this.colour = Paint.valueOf(colour.toUpperCase(Locale.ROOT));
        this.col = colour;
        moving = false;

        strat = (colour.equalsIgnoreCase("red")) ? new RedStrategy() :
                (colour.equalsIgnoreCase("blue")) ? new BlueStrategy() : new WhiteStrategy();

    }

    public void setMoving(boolean moving) {this.moving = moving;}

    public boolean getMoving() {
        return (xVel == 0 && yVel == 0) ? false : true;
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

    @Override
    public void setX(double x) {
        this.xPos = x;
    }

    @Override
    public void setY(double y) {
        this.yPos = y;
    }

    @Override
    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    @Override
    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public void setRadius(double raidus) {this.radius = radius;}

    public void move(double friction) {
        this.setX((this.getX() + this.getxVel() / 60));
        this.setY((this.getY() +  this.getyVel() / 60));
    }

    public void executeStrat(){
        strat.check(this);
    }
}
