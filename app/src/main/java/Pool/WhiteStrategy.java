package Pool;


import java.util.List;

import static Pool.Reader.parseBall;

public class WhiteStrategy implements Strategy{
    @Override
    public void check(Ball ball, Table t) {
        if (((BasicTable) t).getBalls().size() != 1){
            ((BasicTable) t).reset();


        }
        else{
            ((BasicTable) t).setPlaying(false);
        }
    }
}
