package Pool;

import java.util.List;

public interface Ball {

    double getX();
    double getY();
    double getxVel();
    double getyVel();
    double getMass();
    double getRadius();
    void setRadius(double r);
    void setX(double x);
    void setY(double y);
    void setxVel(double xVel);
    void setyVel(double yVel);
    boolean getMoving();
    String getCol();
    void move(double friction);
    void executeStrat(Table t);
    double getOriginalX();
    double getOriginalY();
    void reset();
    boolean exist();


}
