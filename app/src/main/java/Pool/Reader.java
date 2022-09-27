package Pool;

import Factory.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Reader {

    private BallBuilder builder;
    private String path = "Database/config.json";

    public List<Ball> getBalls() {return balls;}

    private List<Ball> balls = new ArrayList<>();

    public Table parseTable() {

        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader(this.path));

            JSONObject jobj = (JSONObject) obj;

            JSONObject jTable = (JSONObject) jobj.get("Table");

            String tableColour = (String) jTable.get("colour");

            Double tableFriction = (Double) jTable.get("friction");

            Double tableX = (Double) ((JSONObject) jTable.get("size")).get("x");

            Double tableY = (Double) ((JSONObject) jTable.get("size")).get("y");

            TableFactory tableF = new BasicTableFactory();

            return tableF.create(tableX, tableY, tableFriction, tableColour);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;
    }

    public List<Ball> parseBall() {

        // initialise a new arraylist here to avoid duplicate calling
        balls = new ArrayList<>();

        JSONParser parser = new JSONParser();

        ConcreteBallBuilder cb = new ConcreteBallBuilder();

        BasicBallFactory ballFactory = new BasicBallFactory(cb);

        try{
            Object object = parser.parse(new FileReader(path));

            JSONObject jsonObject = (JSONObject) object;

            JSONObject jsonBalls = (JSONObject) jsonObject.get("Balls");

            JSONArray jsonBallsBall = (JSONArray) jsonBalls.get("ball");

            int i = 1;

            for (Object obj: jsonBallsBall) {

                JSONObject jsonBall = (JSONObject) obj;

                String colour = ((String) jsonBall.get("colour")).toUpperCase(Locale.ROOT);

                Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");
                Double positionY = (Double) ((JSONObject) jsonBall.get("position")).get("y");
                Double velocityX = (Double) ((JSONObject) jsonBall.get("velocity")).get("x");
                Double velocityY = (Double) ((JSONObject) jsonBall.get("velocity")).get("y");
                Double mass = (Double) jsonBall.get("mass");

                balls.add(ballFactory.create(positionX, positionY, velocityX, velocityY, mass, colour));
            }
            return balls;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void main(String[] args) {

        Reader asd = new Reader();

        System.out.println(asd.parseBall().size());
    }

}