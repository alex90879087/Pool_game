package Pool;


import java.util.List;

import static Pool.Reader.parseBall;

public class WhiteStrategy implements Strategy{
    @Override
    public void check(Ball ball, GameWindow g) {

        if (g.getBalls().size() == 1){
            g.setBalls(parseBall());
        }
        else{
            System.out.println("Game over");
        }
    }
}
