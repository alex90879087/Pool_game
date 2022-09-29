package Pool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.security.Key;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static Pool.Reader.parseBall;

public class GameWindow {

    private final GraphicsContext gc;
    private Scene scene;
    private BasicTable model;
    private Pane pane;
    private  double height;
    private  double  width;
    private Line currentLine;
    private List<Ball> balls;
    private boolean status;




    // to record the start position when click on the cue ball
    private double startX;
    private double startY;

    private colBall cueBall;
    private List<Circle> pockets = new ArrayList<>();

    public List<Ball> getBalls() { return balls;}
    public void setBalls(List<Ball> balls) {this.balls = balls;}

    GameWindow(BasicTable table){
        this.model = table;
        this.pane = new Pane();
        height = table.getY();
        width = table.getX();
        this.scene = new Scene(this.pane, width, height);
        Canvas canvas = new Canvas(width,  height);
        gc = canvas.getGraphicsContext2D();
        this.cueBall = (colBall) this.model.getCueBall();
        this.balls = model.getBalls();
        this.pockets = model.getPockets();
        this.status = model.getPlaying();

        // pane is the lowest level in the hierarchy in this case
        pane.getChildren().add(canvas);
    }

    void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                a -> {
                    this.draw();
//                  can only click cue ball when it is not moving
                    pane.setOnMousePressed(e -> {

                        if (model.getCueBall().getMoving() == false && cursorCueBall(e.getX(), e.getY())) {

                            currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                            pane.getChildren().add(currentLine);

                            pane.setOnMouseDragged(d -> {
                                if (cueBall.getMoving() == false) {
                                    if (currentLine == null) {
                                        currentLine = new Line();
                                        currentLine.setStartX(startX);
                                        currentLine.setStartY(startY);
                                    }
                                    else {
                                        currentLine.setEndX(d.getX());
                                        currentLine.setEndY(d.getY());
                                    }
                                }
                            });
                            pane.setOnMouseReleased(q -> {
                                if (cueBall.getMoving() == false) {
                                    double distance = Math.hypot(startX - q.getX(), startY - q.getY());
                                    shoot(distance);
                                    pane.getChildren().remove(currentLine);
                                }
                            });
                        }
                    });

                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        if (!status) {
            System.out.println("Game over");
            timeline.stop();
        }
    }



    // distance of start and end point is used to calculate the power of the shot
    public void shoot(double distance) {

        // power of the shot
        double power = (distance < 250) ? 3.5 : (distance < 450) ? 4 : 4.5;

        // direction of the mouse dragging (complex number vector)
        this.cueBall.setyVel((-(currentLine.getEndY() - currentLine.getStartY())) / 5 * power);
        this.cueBall.setxVel((-(currentLine.getEndX() - currentLine.getStartX())) / 5 * power);

    }


    private void draw() {

        model.inPocket();
        this.cueBall = (colBall) this.model.getCueBall();
        model.tick();
        model.SlowDown(this.model.getFriction());
        model.moves();

        gc.clearRect(0,0, width, height);
        gc.setFill(model.getColour());
        gc.fillRect(0,0, model.getX(),model.getY());


        for (Circle c: this.pockets) {
            gc.setFill(Paint.valueOf("BLACK"));
            gc.fillOval(c.getCenterX() - c.getRadius(),
                    c.getCenterY() - c.getRadius(),
                    c.getRadius() * 2,
                    c.getRadius() * 2);
        }


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

    boolean cursorCueBall(double x, double y) {
        return (x < cueBall.getX() + cueBall.getRadius() &&
                x > cueBall.getX() - cueBall.getRadius()) &&
               (y < cueBall.getY() + cueBall.getRadius() &&
                y > cueBall.getY() - cueBall.getRadius());
    }


}
