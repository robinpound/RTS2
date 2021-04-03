package code;


import javafx.event.EventHandler;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Database {
    private Connection connection = null;
    private Statement statement;
    private HashMap<String, Double> returningrocketinputs = new HashMap<>();
    private HashMap<String, Double> returningenvironmentinputs = new HashMap<>();
    Database(String url) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //statement.executeUpdate("DROP TABLE users");
            statement.executeUpdate("" +
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "   userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   username STRING," +
                    "   password STRING)"
            );
            statement.executeUpdate("" +

                    "CREATE TABLE IF NOT EXISTS rockets (" +
                    "   recordID INTEGER PRIMARY KEY AUTOINCREMENT," + //recordID
                    "   username STRING," +   //username
                    "   title STRING," +      //title
                    "   creationdate DATE," + //date
                    "   password STRING," +   //password
                    "   fuelmass REAL," +     //data
                    "   hullmass REAL," +     //data
                    "   enginemass REAL," +   //data
                    "   payloadmass REAL," +  //data
                    "   enginethrust REAL," + //data
                    "   burnrate REAL," +     //data
                    "   nosediameter REAL," + //data
                    "   dragcoefficient REAL)"//data
            );
            statement.executeUpdate("" +

                    "CREATE TABLE IF NOT EXISTS environments (" +
                    "   recordID INTEGER PRIMARY KEY AUTOINCREMENT," + //recordID
                    "   username STRING," +   //username
                    "   title STRING," +      //title
                    "   creationdate DATE," + //date
                    "   password STRING," +   //password
                    "   timestep REAL," +     //data
                    "   playbackspeed REAL," +     //data
                    "   simulationduration REAL," +   //data
                    "   windspeed REAL," +  //data
                    "   windangle REAL," + //data
                    "   altitude REAL," +     //data
                    "   azimuth REAL," + //data
                    "   latitude REAL," +//data
                    "   longitude REAL)"      //data
            );
            //statement.executeUpdate("CREATE TABLE IF NOT EXISTS environment (id INTEGER PRIMARY KEY AUTOINCREMENT, username STRING, password STRING)")
        }catch(SQLException e) {System.err.println(e.getMessage());}
    }
    public Boolean LoginUserCheck(String username, String password){
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT (*) FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
            System.out.println(result.getInt(1));
            if (result.getInt(1) > 0) {
                return true;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }
    public Boolean RegisterUser(String username, String password1, String password2){
        try{
            //Trimming
            String trailing_space = "\\s+$";
            String leading_space = "^\\s+";
            String trimmed_username = username.replaceAll(trailing_space,"").replaceAll(leading_space,"");

            ResultSet result = statement.executeQuery("SELECT COUNT (*) FROM users WHERE username = '" + trimmed_username + "'");//check for existence
            System.out.println(trimmed_username.length());
            if (result.getInt(1) > 0 || !password1.equals(password2) || trimmed_username.length() == 0 || password1.length() == 0) {
                return false;
            }else{
                statement.executeUpdate(String.format("INSERT INTO users VALUES(null, '%s', '%s')",trimmed_username,password1));
                return true;
            }
        }catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }

    public void insertRocketRecord(Stage primaryStage, HashMap<String, NormalTextBox> Hashmap, String saved_username){

        NormalUserInterface SavingUI = new NormalUserInterface(160, 380, primaryStage);
        SavingUI.GetStage().setTitle("Save As");
        SavingUI.createGridPane(250, 2, 2);
        SavingUI.addStageDimensions();

        Double fuelmass = Double.parseDouble(Hashmap.get("Fuel Mass").getValue());
        Double hullmass = Double.parseDouble(Hashmap.get("Hull Mass").getValue());
        Double enginemass = Double.parseDouble(Hashmap.get("Engine Mass").getValue());
        Double payloadmass = Double.parseDouble(Hashmap.get("Payload Mass").getValue());
        Double enginethrust = Double.parseDouble(Hashmap.get("Engine Thrust").getValue());
        Double burnrate = Double.parseDouble(Hashmap.get("Burn Rate").getValue());
        Double nosediameter = Double.parseDouble(Hashmap.get("Nose Diameter").getValue());
        Double dragcoefficient = Double.parseDouble(Hashmap.get("Drag Coefficient").getValue());

        SavingUI.addText("Save Rocket Parameters:", 20, 3, 1);
        SavingUI.addFieldToTheGrid("Title", " ");
        SavingUI.addFieldToTheGrid("Record Password", "(optional)");
        SavingUI.NormalFieldHashMap.get("Record Password").setCoordinatespassword(0, 2);
        SavingUI.addButtonToTheGrid("SAVE", 1, 1);
        SavingUI.NormalButtonHashMap.get("SAVE").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String title = SavingUI.NormalFieldHashMap.get("Title").getValue();
                    String password = SavingUI.NormalFieldHashMap.get("Record Password").getThing4().getText();
                    String date = statement.executeQuery("SELECT date('now')").getString(1);
                    System.out.println(title + "   " + date + " !!!!!!!!!!");
                    statement.executeUpdate(String.format("INSERT INTO rockets VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", saved_username, title, date, password, fuelmass, hullmass, enginemass, payloadmass, enginethrust, burnrate, nosediameter, dragcoefficient));
                    SavingUI.GetStage().close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

        SavingUI.Configure2D();
        SavingUI.GetStage().showAndWait();

    }
    public HashMap<String, Double> getRocketRecord(Stage primaryStage){

        NormalUserInterface LoadingUI = new NormalUserInterface(500, 800, primaryStage);
        LoadingUI.GetStage().setTitle("Rocket Records");
        LoadingUI.addStageDimensions();
        LoadingUI.createGridPane(400,1,1);
        LoadingUI.createrocketTable(statement);
        LoadingUI.addButtonToTheGrid("LOAD",1,2);
        LoadingUI.NormalButtonHashMap.get("LOAD").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returningrocketinputs = LoadingUI.getSelectedRocketHashmap();
                if (returningrocketinputs.size() != 0){
                    LoadingUI.GetStage().close();
                }
            }
        });
        LoadingUI.Configure2D();
        LoadingUI.GetStage().showAndWait();

        return returningrocketinputs;
    }

    public void insertEnvironmentRecord(Stage primaryStage, HashMap<String, NormalTextBox> Hashmap, String saved_username){

        NormalUserInterface SavingUI = new NormalUserInterface(160, 380, primaryStage);
        SavingUI.GetStage().setTitle("Save As");
        SavingUI.createGridPane(250, 2, 2);
        SavingUI.addStageDimensions();

        Double timestep = Double.parseDouble(Hashmap.get("Time Step").getValue());
        Double playbackspeed = Double.parseDouble(Hashmap.get("Playback Speed").getValue());
        Double simulationduration = Double.parseDouble(Hashmap.get("Simulation Duration").getValue());
        Double windspeed = Double.parseDouble(Hashmap.get("Wind Speed").getValue());
        Double windangle = Double.parseDouble(Hashmap.get("Wind Angle").getValue());
        Double altitude = Double.parseDouble(Hashmap.get("Altitude").getValue());
        Double azimuth = Double.parseDouble(Hashmap.get("Azimuth").getValue());
        Double latitude = Double.parseDouble(Hashmap.get("Latitude").getValue());
        Double longitude = Double.parseDouble(Hashmap.get("Longitude").getValue());

        SavingUI.addText("Save Environment Parameters:", 20, 3, 1);
        SavingUI.addFieldToTheGrid("Title", " ");
        SavingUI.addFieldToTheGrid("Record Password", "(optional)");
        SavingUI.NormalFieldHashMap.get("Record Password").setCoordinatespassword(0, 2);
        SavingUI.addButtonToTheGrid("SAVE", 1, 1);
        SavingUI.NormalButtonHashMap.get("SAVE").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String title = SavingUI.NormalFieldHashMap.get("Title").getValue();
                    String password = SavingUI.NormalFieldHashMap.get("Record Password").getThing4().getText();
                    String date = statement.executeQuery("SELECT date('now')").getString(1);
                    statement.executeUpdate(String.format("INSERT INTO environments VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", saved_username, title, date, password, timestep, playbackspeed, simulationduration, windspeed, windangle, altitude, azimuth, latitude, longitude));
                    SavingUI.GetStage().close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

        SavingUI.Configure2D();
        SavingUI.GetStage().showAndWait();

    }
    public HashMap<String, Double> getEnvironmentRecord(Stage primaryStage){

        NormalUserInterface LoadingUI = new NormalUserInterface(500, 800, primaryStage);
        LoadingUI.GetStage().setTitle("Environment Records");
        LoadingUI.addStageDimensions();
        LoadingUI.createGridPane(400,1,1);
        LoadingUI.createenvironmentTable(statement);
        LoadingUI.addButtonToTheGrid("LOAD",1,2);
        LoadingUI.NormalButtonHashMap.get("LOAD").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returningenvironmentinputs = LoadingUI.getSelectedEnvironmentHashmap();
                if (returningenvironmentinputs.size() != 0){
                    LoadingUI.GetStage().close();
                }
            }
        });
        LoadingUI.Configure2D();
        LoadingUI.GetStage().showAndWait();

        return returningenvironmentinputs;
    }

    public void closeDatabase(){

        try{if(connection != null) connection.close();}
        catch(SQLException e){System.err.println(e.getMessage());}
    }

}
