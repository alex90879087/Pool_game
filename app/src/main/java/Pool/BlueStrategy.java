package Pool;

import java.util.List;

public class BlueStrategy implements Strategy{
    @Override
    public void check(Ball ball, GameWindow g) {
        ball = (colBall) ball;

        if (((colBall) ball).getCount() != 0) {
            ((colBall) ball).setOriginalXY();
            ((colBall) ball).setCount();
            ball.setyVel(0);
            ball.setxVel(0);
        }
        else{
            g.getBalls().remove(ball);
        }
    }
}
