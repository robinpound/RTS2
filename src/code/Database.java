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
                    "   rocketid INTEGER PRIMARY KEY AUTOINCREMENT," + //rocketid
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
        NormalUserInterface LoadingUI = new NormalUserInterface(300, 400, primaryStage);
        LoadingUI.GetStage().setTitle("Rocket Records");
        LoadingUI.createGridPane(250, 2, 2);
        LoadingUI.addStageDimensions();




        LoadingUI.Configure2D();
        LoadingUI.GetStage().showAndWait();
        return null;
    }
    public void closeDatabase(){

        try{if(connection != null) connection.close();}
        catch(SQLException e){System.err.println(e.getMessage());}
    }

}
