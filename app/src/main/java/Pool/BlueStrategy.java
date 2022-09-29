package Pool;

import java.util.List;

public class BlueStrategy implements Strategy{
    @Override
    public void check(Ball ball) {
        ball = (colBall) ball;

        if (((colBall) ball).getCount() != 0) {
            ball.setX(ball.getOriginalX());
            ball.setY(ball.getOriginalY());
        }
        else{

        }
    }
}
