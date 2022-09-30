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
    private static String path = "app/src/main/resources/config.json";

    //  C:\Users\alex9\Pool\app\src\main\resources\config.json
    private static List<Ball> balls = new ArrayList<>();

    public static Table parseTable() {

        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader(path));

            JSONObject jObj = (JSONObject) obj;

            JSONObject jTable = (JSONObject) jObj.get("Table");

            String tableColour = (String) jTable.get("colour");

            Double tableFriction = (Double) jTable.get("friction");

            Long tableX = (Long) ((JSONObject) jTable.get("size")).get("x");

            Long tableY = (Long) ((JSONObject) jTable.get("size")).get("y");

            TableFactory tableF = new ConcreteTableFactory();

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

    public static List<Ball> parseBall() {

        // initialise a new arraylist here to avoid duplicate calling
        balls = new ArrayList<>();

        JSONParser parser = new JSONParser();

        ConcreteBallBuilder cb = new ConcreteBallBuilder();

        ConcreteBasicBallFactory ballFactory = new ConcreteBasicBallFactory(cb);

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


}
