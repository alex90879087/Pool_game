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
    private Line visualCue;
    private List<Ball> balls;

    // to record the start position when click on the cue ball
    private double startX;
    private double startY;

    private ColBall cueBall;


    GameWindow(BasicTable table){
        this.model = table;
        this.pane = new Pane();
        height = table.getY();
        width = table.getX();
        this.scene = new Scene(this.pane, width, height);
        Canvas canvas = new Canvas(width,  height);
        gc = canvas.getGraphicsContext2D();
        this.cueBall = (ColBall) this.model.getCueBall();
        this.balls = model.getBalls();

        pane.getChildren().add(canvas);
    }

    public void run() {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(17),
                a -> {

                    this.draw();
                    if (!model.getCueBall().getMoving()) System.out.println("Hit cue ball!");
                    else System.out.println("Not yet");

                    pane.setOnMousePressed(e -> {

                        if (!model.getCueBall().getMoving() && cursorCueBall(e.getX(), e.getY())) {

                            visualCue = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                            pane.getChildren().add(visualCue);

                            pane.setOnMouseDragged(d -> {
                                if (!cueBall.getMoving()) {
                                    if (visualCue == null) {
                                        visualCue = new Line();
                                        visualCue.setStartX(startX);
                                        visualCue.setStartY(startY);
                                    }
                                    else {
                                        visualCue.setEndX(d.getX());
                                        visualCue.setEndY(d.getY());
                                    }
                                }
                            });
                            pane.setOnMouseReleased(q -> {
                                if (!cueBall.getMoving()) {
                                    double distance = Math.hypot(startX - q.getX(), startY - q.getY());
                                    shoot(distance);
                                    pane.getChildren().remove(visualCue);
                                }
                            });
                        }
                    });

                    // player wins when only cue ball left
                if (model.getBalls().size() == 1) {
                    timeline.stop();
                    System.out.println("YOU　WIN!");
                }
                });
        timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(17)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void shoot(double distance) {

        // power of the shot
        double power = (distance < 250) ? 3.5 : (distance < 450) ? 4 : 4.5;

        // complex number vector
        this.cueBall.setyVel((-(visualCue.getEndY() - visualCue.getStartY())) / 5 * power);
        this.cueBall.setxVel((-(visualCue.getEndX() - visualCue.getStartX())) / 5 * power);
    }

    private void draw() {

        model.inPocket();
        // update to new cueBall's memory address if necessary (when cue ball fall into pockets and whole set of ball get reset,
        // which done by parseBall() that returns "new" set of ball)
        this.cueBall = (ColBall) this.model.getCueBall();
        model.tick();
        model.SlowDown(this.model.getFriction());
        model.moves();

        gc.clearRect(0,0, width, height);
        gc.setFill(model.getColour());
        gc.fillRect(0,0, model.getX(),model.getY());

        for (Circle c: model.getPockets()) {
            gc.setFill(Paint.valueOf("BLACK"));
            gc.fillOval(c.getCenterX() - c.getRadius(),
                    c.getCenterY() - c.getRadius(),
                    c.getRadius() * 2,
                    c.getRadius() * 2);
        }

        for (Ball ball: model.getBalls()){
            gc.setFill(((ColBall) ball).getColour());
            gc.fillOval(ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    ball.getRadius() * 2,
                    ball.getRadius() * 2);
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    // check if cursor enters cue ball area
    public boolean cursorCueBall(double x, double y) {
        return (x < cueBall.getX() + cueBall.getRadius() &&
                x > cueBall.getX() - cueBall.getRadius()) &&
               (y < cueBall.getY() + cueBall.getRadius() &&
                y > cueBall.getY() - cueBall.getRadius());
    }


}
