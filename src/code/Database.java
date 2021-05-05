package code;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private Boolean CorrectPassword = true;
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
            String trimmed_username = username.trim();
            if (trimmed_username.length() == 0 || password.length() == 0) {
                return false;
            }
            String query = "SELECT COUNT (*) FROM users WHERE username=? AND password=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trimmed_username);
            preparedStatement.setString(2, password);
            //System.out.println(preparedStatement.toString());
            ResultSet result = preparedStatement.executeQuery();
            //System.out.println(result.getString(1));
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
            String trimmed_username = username.trim();
            if (trimmed_username.length() == 0 || password1.length() == 0 || !password1.equals(password2)) {
                return false;
            }
            String query = "SELECT COUNT (*) FROM users WHERE username=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trimmed_username);
            //System.out.println(preparedStatement.toString());
            ResultSet result = preparedStatement.executeQuery();
            if (result.getInt(1) > 0) {
                return false;
            }
            String update = "INSERT INTO users VALUES(null, ?, ?)";
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, trimmed_username);
            preparedStatement.setString(2, password1);
            preparedStatement.executeUpdate();
            //System.out.println(preparedStatement.toString());
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage());
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
                    //System.out.println(title + "   " + date + " !!!!!!!!!!");
                    String update = String.format("INSERT INTO rockets VALUES(null, ?, ?, ?, ?," +
                                    " '%f', '%f', '%f', '%f', '%f', '%f', '%f', '%f')",
                            fuelmass, hullmass, enginemass, payloadmass, enginethrust,
                            burnrate, nosediameter, dragcoefficient);
                    preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setString(1, saved_username.trim());
                    preparedStatement.setString(2, title);
                    preparedStatement.setString(3, date);
                    preparedStatement.setString(4, password);
                    preparedStatement.executeUpdate();
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
                    if (LoadingUI.getSelectedRocketPassword().length() != 0) {
                        String password = LoadingUI.getSelectedRocketPassword();
                        passwordcheck(primaryStage, password);
                    }
                    if (CorrectPassword){
                        LoadingUI.GetStage().close();
                    }
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
                    String update = String.format("INSERT INTO environments VALUES(null, ?, ?, ?, ?," +
                                    " '%f', '%f', '%f', '%f', '%f', '%f', '%f', '%f', '%f')",
                            timestep, playbackspeed, simulationduration, windspeed,
                            windangle, altitude, azimuth, latitude, longitude);
                    preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setString(1, saved_username.trim());
                    preparedStatement.setString(2, title);
                    preparedStatement.setString(3, date);
                    preparedStatement.setString(4, password);
                    preparedStatement.executeUpdate();
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
                    if (LoadingUI.getSelectedEnvironmentPassword().length() != 0) {
                        String password = LoadingUI.getSelectedEnvironmentPassword();
                        passwordcheck(primaryStage, password);
                    }
                    if (CorrectPassword){
                        LoadingUI.GetStage().close();
                    }
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

    public void passwordcheck(Stage primaryStage, String password){
        CorrectPassword = false;
        NormalUserInterface setPasswordUI = new NormalUserInterface(150,300, primaryStage);
        setPasswordUI.GetStage().setTitle("Password:");
        setPasswordUI.addStageDimensions();
        setPasswordUI.createGridPane(400,1,1);
        setPasswordUI.addText("    Enter Password   ",30,3,1);
        setPasswordUI.addFieldToTheGrid("Password"," ");
        setPasswordUI.NormalFieldHashMap.get("Password").setCoordinatespassword(0,2);
        setPasswordUI.addButtonToTheGrid("Enter",3,1);
        setPasswordUI.NormalButtonHashMap.get("Enter").setCoordinates(1,3);
        setPasswordUI.NormalButtonHashMap.get("Enter").translateObject(0,20);
        setPasswordUI.NormalButtonHashMap.get("Enter").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                //System.out.println(setPasswordUI.NormalFieldHashMap.get("Password").getThing4().getText() + password);
                if (password.equals(setPasswordUI.NormalFieldHashMap.get("Password").getThing4().getText())){
                    //System.out.println(setPasswordUI.NormalFieldHashMap.get("Password").getThing4().getText() + password);
                    CorrectPassword = true;
                    setPasswordUI.GetStage().close();
                }
            }
        });
        setPasswordUI.Configure2D();
        setPasswordUI.GetStage().showAndWait();
    }
}
