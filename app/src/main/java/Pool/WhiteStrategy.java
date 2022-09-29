package Pool;


import java.util.List;

import static Pool.Reader.parseBall;

public class WhiteStrategy implements Strategy{
    @Override
    public void check(Ball ball, GameWindow g) {
        if (g.getBalls().size() != 1){
            for (Ball eachBall: g.getBalls()) {
                ((colBall) eachBall).setOriginalXY();
                eachBall.setxVel(0);
                eachBall.setyVel(0);
            }
        }
        else{
            g.setStatus(false);
        }
    }
}
