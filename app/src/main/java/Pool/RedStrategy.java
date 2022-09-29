package Pool;

import java.util.List;

public class RedStrategy implements Strategy{
    @Override
    public void check(Ball ball) {

        // check if ball is in the pockets

        ball.setX(10000);

    }
}
