package Pool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.security.Key;

public class GameWindow {

    private final GraphicsContext gc;
    private Scene scene;
    private BasicTable model;
    private Pane pane;
    private final int length = 1920;
    private final int width = 1080;


    GameWindow(BasicTable table){
        this.model =  table;
        this.pane = new Pane();
        this.scene = new Scene(this.pane, this.length, this.width);
        Canvas canvas = new Canvas(this.length, this.width);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
    }

    void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {

        gc.clearRect(0,0, this.length, this.width);

        for (Ball ball: model.getBalls()){
            ball = (colBall) ball;
            ((colBall) ball).getColour();
            gc.fillOval(ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    ball.getRadius() * 2,
                    ball.getRadius() * 2);
        }
    }

    public Scene getScene() {
        return this.scene;
    }
}
