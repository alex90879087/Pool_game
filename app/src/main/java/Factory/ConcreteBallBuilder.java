package Factory;

import Factory.BallBuilder;
import Pool.Ball;
import Pool.ColBall;

public class ConcreteBallBuilder implements BallBuilder {

    private Ball ball;
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double mass;
    private String col;

    public String getCol(){ return col;}


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

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public void setColour(String colour) {
        this.col = colour;
    }


    public Ball build() {
        return new ColBall(xPos, yPos, xVel, yVel, mass, col);
    }
}
