package Pool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.security.Key;

public class GameWindow {

    private final GraphicsContext gc;
    private Scene scene;
    private BasicTable model;
    private Pane pane;
    private final int length = 1920;
    private final int width = 1080;
    private Line currentLine;

    // to record the start position when click on the cue ball
    private double startX;
    private double startY;

    private colBall cueBall;



    GameWindow(BasicTable table){
        this.model =  table;
        this.pane = new Pane();
        this.scene = new Scene(this.pane, table.getX(), table.getY());
        Canvas canvas = new Canvas(table.getX(),  table.getY());
        gc = canvas.getGraphicsContext2D();
        this.cueBall = (colBall) this.model.getCueBall();

        // pane is the lowest level in the hierarchy in this case
        pane.getChildren().add(canvas);
    }

    void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                a -> {
                    this.draw();

//                  can only click cue ball when it is not moving
                    pane.setOnMousePressed(e -> {
                        startX = e.getX();
                        startY = e.getY();
                    });

                    pane.setOnMouseDragged(e -> {
                        if (cueBall.getMoving() == false) {

                        }
                    });


                }));


        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void initDraw(GraphicsContext gc, double x, double y) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();
        gc.strokeRect(x, // x of the upper left corner
                y, // y of the upper left corner
                canvasWidth, // width of the rectangle
                canvasHeight); // height of the rectangle

    }
    private void draw() {
        gc.clearRect(0,0, this.length, this.width);



        gc.setFill(model.getColour());
        gc.fillRect(0,0, model.getX(),model.getY());


        for (Ball ball: model.getBalls()){
            gc.setFill(((colBall) ball).getColour());
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
