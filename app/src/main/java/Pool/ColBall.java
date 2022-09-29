package Pool;

import javafx.scene.paint.Paint;

import java.util.Locale;

public class ColBall implements Ball {
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double mass;
    private double radius = 12;
    private Paint colour;
    private String col;
    private Strategy strat;

    private double originalX;
    private double originalY;

    private int count = 1;

    private boolean exist;


    public ColBall (double xPos, double yPos, double xVel, double yVel, double mass, String colour) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.mass = mass;
        this.colour = Paint.valueOf(colour.toUpperCase(Locale.ROOT));
        this.col = colour;
        this.originalX = xPos;
        this.originalY = yPos;
        this.exist = true;

        strat = (colour.equalsIgnoreCase("red")) ? new RedStrategy() :
                (colour.equalsIgnoreCase("blue")) ? new BlueStrategy() : new WhiteStrategy();

    }


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

    public void setColour(String col) {
        this.col = col;
        this.colour = Paint.valueOf(col.toUpperCase(Locale.ROOT));
    }

    public void move(double friction) {
        this.setX((this.getX() + this.getxVel() / 60));
        this.setY((this.getY() +  this.getyVel() / 60));
    }

    public void executeStrat(Table t){
        strat.check(this, t);
    }

    @Override
    public double getOriginalX() {
        return this.originalX;
    }

    @Override
    public double getOriginalY() {
        return this.originalY;
    }

    public void setCount() {this.count -= 1;}

    public double getCount() {return this.count;}

    public void reset(){
        exist = false;
    }

    @Override
    public boolean exist() {
        return this.exist;
    }

    public void setOriginalXY() {
        this.xPos = originalX;
        this.yPos = originalY;
    }


}

