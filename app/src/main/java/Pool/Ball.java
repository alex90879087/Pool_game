package Pool;

public interface Ball {

    double getX();
    double getY();
    double getxVel();
    double getyVel();
    double getMass();
    double getRadius();
    void setX(double x);
    void setY(double y);
    void setxVel(double xVel);
    void setyVel(double yVel);
    boolean getMoving();
    String getCol();
    void move(double friction);
    void executeStrat();
}
