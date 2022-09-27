package Factory;

import Pool.Ball;

public interface BallFactory {
    Ball create(double x, double y, double xVel, double yVel, double mass, String col);
}
