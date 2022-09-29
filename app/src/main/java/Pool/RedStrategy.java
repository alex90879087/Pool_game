package Pool;

import javafx.scene.shape.Circle;

import java.util.List;

public class RedStrategy implements Strategy{
    @Override
    public void check(Ball ball) {

        // check if ball is in the pockets
        ball.setxVel(0);
        ball.setyVel(0);

        ball.setX(-5000);
        ball.setY(-5000);

    }
}
