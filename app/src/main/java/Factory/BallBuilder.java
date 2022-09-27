package Factory;

import Pool.Ball;

public interface BallBuilder {

    void reset();
    void setX(double x);
    void setY(double y);
    void setxVel(double xVel);
    void setyVel(double yVel);
    void setMass(double mass);
    void setColour(String colour);

    Ball build();
}
