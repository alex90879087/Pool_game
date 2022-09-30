package Pool;

import java.util.List;

public class RedStrategy implements Strategy{
    @Override
    public void check(Ball ball, Table t) {

        // check if ball is in the pockets
        ball.reset();
    }
}
