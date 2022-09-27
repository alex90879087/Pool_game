package Factory;

import Pool.Ball;

public class BasicBallFactory implements BallFactory {

    private ConcreteBallBuilder cb = new ConcreteBallBuilder();


    public BasicBallFactory(ConcreteBallBuilder cb) {
        this.cb = cb;
    }
    @Override
    public Ball create(double x, double y, double xVel, double yVel, double mass, String col) {

        cb.setX(x);
        cb.setY(y);
        cb.setxVel(xVel);
        cb.setY(yVel);
        cb.setMass(mass);
        cb.setColour(col);

        return cb.build();
    }
}
