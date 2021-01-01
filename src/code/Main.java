package code;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {

    Boolean CloseClicked = false; //need to use to close windows using X
    @Override
    public void start(Stage primaryStage) throws Exception{
        LoginMenu(primaryStage);
        HashMap<String, Double> inputs = BuildingMenu(primaryStage);
        Rocket theRocket = CalculateTrajectory(inputs);
        SimulationMenu(primaryStage, theRocket);
    }
    private void LoginMenu(Stage primaryStage){
        NormalUserInterface LoginUI = new NormalUserInterface(500, 650, primaryStage);
        LoginUI.GetStage().setTitle("RTS-2");
        LoginUI.createGridPane(250,2,2);
        LoginUI.addStageDimensions();
        int REGISTERCOLUMN = 3;
        LoginUI.setGap(10,0);
        LoginUI.addText("3D Rocket Trajectory Simulator - 2", 40, 3, 1);

        LoginUI.addText("Login:", 25, 1, 1);
        LoginUI.addFieldToTheGrid("Username"," ");
        LoginUI.addFieldToTheGrid("Password"," ");
        LoginUI.addButtonToTheGrid("LOGIN",1,1);

        LoginUI.addCoordinateText("Register:", 25,REGISTERCOLUMN,1,1,1);

        LoginUI.addFieldToTheGrid("New Username"," ");
        LoginUI.NormalFieldHashMap.get("New Username").setCoordinates(REGISTERCOLUMN, 2);

        LoginUI.addFieldToTheGrid("New Password", " ");
        LoginUI.NormalFieldHashMap.get("New Password").setCoordinates(REGISTERCOLUMN,3);

        LoginUI.addFieldToTheGrid("Re-type New Password"," ");
        LoginUI.NormalFieldHashMap.get("Re-type New Password").setCoordinates(REGISTERCOLUMN,4);

        LoginUI.addButtonToTheGrid("REGISTER",1,1);
        LoginUI.NormalButtonHashMap.get("REGISTER").setCoordinates(REGISTERCOLUMN, 5);


        //LoginUI.addText(" ", 65, 3, 3);
        LoginUI.addButtonToTheGrid("Exit", 1,1);
        LoginUI.NormalButtonHashMap.get("Exit").setCoordinates(4,7);
        LoginUI.NormalButtonHashMap.get("Exit").translateObject(165,52);
        LoginUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
        LoginUI.Configure2D();
        LoginUI.GetStage().showAndWait();
    }
    private HashMap<String, Double> BuildingMenu(Stage primaryStage){

        HashMap<String, Double> inputs = new HashMap<String, Double>();

        //set up Stage

        NormalUserInterface FirstUI = new NormalUserInterface(650, 950, primaryStage);
        FirstUI.GetStage().setTitle("Building Menu");
        //Initial configurations
        FirstUI.createGridPane(250, 0, 2);
        FirstUI.addStageDimensions();
        FirstUI.addCameraAndSubscene();
        FirstUI.setSimulation();
        FirstUI.Configure();

        FirstUI.addText("Launching Statistics:", 30, 3, 1);
        FirstUI.addNormalDataToTheGrid("Total Rocket Mass", "kg");
        FirstUI.addNormalDataToTheGrid("Delta-V", "m/s");
        FirstUI.addNormalDataToTheGrid("burn_rate", "kg/s");
        FirstUI.addNormalDataToTheGrid("drag-coefficient", "none"); //can we do this as just drag?
        FirstUI.addNormalDataToTheGrid("something", "?");
        //FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(12345);

        FirstUI.addText("________________", 1, 3, 1);
        FirstUI.addText("Edit:", 30, 3, 1);
        FirstUI.addButtonToTheGrid("Rocket", 1,1);
        FirstUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Rocket").Gettext() + " was clicked");
                HashMap<String,Double> Rocketfields = RocketParameterMenu(primaryStage);
            }
        });
        FirstUI.addButtonToTheGrid("Environment", 1,1);
        FirstUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                HashMap<String,Double> Environmentfields = EnvironmentParameterMenu(primaryStage);
            }
        });
        FirstUI.addButtonToTheGrid("Launch", 1,1);
        FirstUI.NormalButtonHashMap.get("Launch").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FirstUI.GetStage().close();
                System.out.println(FirstUI.NormalButtonHashMap.get("Launch").Gettext() + " was clicked");
            }
        });
        FirstUI.addButtonToTheGrid("Exit", 1,1);
        FirstUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Exit").Gettext() + " was clicked");
                System.exit(0);
            }
        });


        FirstUI.GetStage().showAndWait();
        return inputs;

    }
    private Rocket CalculateTrajectory(HashMap<String, Double> inputs){

        Simulation sim = new Simulation();
        double[] values = {0.01, 0.0, 0.0, 0.0, 4.077, 4.0, 0.31, 0.2, 1419.0, 0.474, 90.0, 0.0, 90.0, 0.0};
        sim.run_simulation(values);//hardcode values for now! Change this to hashmap

        return sim.getRocket();
    }
    private void SimulationMenu(Stage primaryStage, Rocket theRocket) {
        System.out.println("Simulation Menu");
        TheAnimationWindow SecondUI = new TheAnimationWindow(700,1000, primaryStage, theRocket);
        SecondUI.GetStage().setTitle("Simulation");

        SecondUI.createGridPane(250,0,2);
        SecondUI.addStageDimensions();
        SecondUI.addCameraAndSubscene();
        SecondUI.setSimulation();
        SecondUI.Configure();
        SecondUI.SetRocketPosition();
        SecondUI.SetCamera();
        SecondUI.SetGround();
        SecondUI.movement();
        SecondUI.RocketMovement();
        SecondUI.createSun();

        SecondUI.addText("Real Time Data:", 30, 3, 1);
        SecondUI.addNormalDataToTheGrid("Location", "m");
        SecondUI.addNormalDataToTheGrid("Velocity", "m/s");
        SecondUI.addNormalDataToTheGrid("Acceleration", ",,");
        SecondUI.addNormalDataToTheGrid("---", ",,");
        SecondUI.addNormalDataToTheGrid("---", ",,");
        //SecondUI.NormalDataHashMap.get("Total Rocket Mass").setText(12345);

        SecondUI.addText("________________", 1, 3, 1);
        SecondUI.addText("Options:", 30, 3, 1);
        SecondUI.addButtonToTheGrid("Rocket", 1,1);
        SecondUI.NormalButtonHashMap.get("Rocket").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Rocket").Gettext() + " was clicked");
                HashMap<String,Double> Rocketfields = RocketParameterMenu(primaryStage);
            }
        });
        SecondUI.addButtonToTheGrid("Environment", 1,1);
        SecondUI.NormalButtonHashMap.get("Environment").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                HashMap<String,Double> Environmentfields = EnvironmentParameterMenu(primaryStage);
            }
        });
        SecondUI.addButtonToTheGrid("Analysis", 1,1);
        SecondUI.NormalButtonHashMap.get("Analysis").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Analysis").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Analysis").Gettext() + " was clicked");
                //launch here
            }
        });
        SecondUI.addButtonToTheGrid("Restart", 1,1);
        SecondUI.NormalButtonHashMap.get("Restart").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Restart").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Restart").Gettext() + " was clicked");
                //restart here
            }
        });
        SecondUI.addButtonToTheGrid("Exit", 1,1);
        SecondUI.NormalButtonHashMap.get("Exit").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Exit").Gettext() + " was clicked");
                System.exit(0);
            }
        });

        SecondUI.GetStage().showAndWait();
    }
    private HashMap<String,Double> RocketParameterMenu(Stage primaryStage) {
        NormalUserInterface ThirdUI = new NormalUserInterface(500, 350, primaryStage);
        ThirdUI.GetStage().setTitle("Rocket Building Menu");
        ThirdUI.createGridPane(250,2,2);
        ThirdUI.addStageDimensions();

        ThirdUI.addText("Rocket:", 3, 3, 1);
        ThirdUI.addFieldToTheGrid("Fuel Mass","kg");
        ThirdUI.addFieldToTheGrid("Dry Mass","kg");
        ThirdUI.addFieldToTheGrid("Engine Thrust","N");
        ThirdUI.addFieldToTheGrid("Burn Rate","kg/s");
        ThirdUI.addFieldToTheGrid("Nose Diameter","m");
        ThirdUI.addFieldToTheGrid("Drag Coefficient","-");

        ThirdUI.addButtonToTheGrid("LOAD",1,1);
        ThirdUI.addButtonToTheGrid("SAVE",1,1);
        ThirdUI.addButtonToTheGrid("SET TO DEFAULT",1,1);
        ThirdUI.addButtonToTheGrid("ACCEPT",1,1);

        ThirdUI.Configure2D();
        ThirdUI.GetStage().showAndWait();
        return null;
    }
    private HashMap<String,Double> EnvironmentParameterMenu(Stage primaryStage) {
        NormalUserInterface FourthUI = new NormalUserInterface(500, 350, primaryStage);
        FourthUI.GetStage().setTitle("Environment Building Menu");
        FourthUI.createGridPane(250,2,2);
        FourthUI.addStageDimensions();

        FourthUI.addText("Simulation:", 3, 3, 1);
        FourthUI.addFieldToTheGrid("Time Step","s");
        FourthUI.addFieldToTheGrid("Playback Speed","-");
        FourthUI.addText("Wind:", 3, 3, 1);
        FourthUI.addFieldToTheGrid("Wind Speed","m/s");
        FourthUI.addFieldToTheGrid("Wind Angle","*");
        FourthUI.addText("Orientation:", 3, 3, 1);
        FourthUI.addFieldToTheGrid("Altitude","*");
        FourthUI.addFieldToTheGrid("Azimuth","*");
        FourthUI.addText("Position:", 3, 3, 1);
        FourthUI.addFieldToTheGrid("Latitude","*");
        FourthUI.addFieldToTheGrid("Longitude","*");

        FourthUI.addButtonToTheGrid("LOAD",1,1);
        FourthUI.addButtonToTheGrid("SAVE",1,1);
        FourthUI.addButtonToTheGrid("SET TO DEFAULT",1,1);
        FourthUI.addButtonToTheGrid("ACCEPT",1,1);

        FourthUI.Configure2D();
        FourthUI.GetStage().showAndWait();
        return null;
    }
    private void OpenHTMLWebsite(){
        File f = new File ("src/code/HelpPage.html");
        try{
            Desktop.getDesktop().browse(f.toURI());
        }catch(IOException e){
            System.out.println(e);
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}



