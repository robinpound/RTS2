package code;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {

    private Boolean CloseClicked = false; //need to use to close windows using X
    private HashMap<String, Double> inputs = new HashMap<String, Double>();
    private Stage primaryStage;
    private double playback_speed;
    private Environment environment;
    private double specificImpulse;

    private double saved_Fuel_Mass; //TURN TO HASHMAP!!!
    private double saved_Hull_Mass;
    private double saved_Engine_Mass;
    private double saved_Payload_Mass;
    private double saved_Engine_Thrust;
    private double saved_Burn_Rate;
    private double saved_Nose_Diameter;
    private double saved_Drag_Coefficient;

    private double saved_Time_Step;  //TURN TO HASHMAP!!!
    private double saved_Playback_Speed;
    private double saved_Simulation_Duration;
    private double saved_Wind_Speed;
    private double saved_Wind_Angle;
    private double saved_Altitude;
    private double saved_Azimuth;
    private double saved_Latitude;
    private double saved_Longitude;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        LoginMenu();
        BuildingMenu();
        Rocket theRocket = CalculateTrajectory();
        SimulationMenu(theRocket); //make theRocket member of the class (private)
    }
    private void LoginMenu(){
        NormalUserInterface LoginUI = new NormalUserInterface(500, 650, primaryStage);
        LoginUI.GetStage().setTitle("RTS-2");
        LoginUI.createGridPane(250,2,2);
        LoginUI.addStageDimensions();
        int REGISTERCOLUMN = 3;
        int REGISTERROW = 2;
        LoginUI.setGap(10,0);
        LoginUI.addText("3D Rocket Trajectory Simulator - 2", 40, 3, 1);
        LoginUI.addText("                                  ", 40, 3, 1);

        LoginUI.addText("Login:", 25, 1, 1);
        LoginUI.addFieldToTheGrid("Username"," ");
        LoginUI.addFieldToTheGrid("Password"," ");
        LoginUI.addButtonToTheGrid("LOGIN",1,1);

        LoginUI.addCoordinateText("Register:", 25,REGISTERCOLUMN,REGISTERROW,1,1);

        LoginUI.addFieldToTheGrid("New Username"," ");
        LoginUI.NormalFieldHashMap.get("New Username").setCoordinates(REGISTERCOLUMN, REGISTERROW+1);

        LoginUI.addFieldToTheGrid("New Password", " ");
        LoginUI.NormalFieldHashMap.get("New Password").setCoordinates(REGISTERCOLUMN,REGISTERROW+2);

        LoginUI.addFieldToTheGrid("Re-type New Password"," ");
        LoginUI.NormalFieldHashMap.get("Re-type New Password").setCoordinates(REGISTERCOLUMN,REGISTERROW+3);

        LoginUI.addButtonToTheGrid("REGISTER",1,1);
        LoginUI.NormalButtonHashMap.get("REGISTER").setCoordinates(REGISTERCOLUMN, REGISTERROW+4);

        LoginUI.addButtonToTheGrid("Run as Guest", 1,1);
        LoginUI.NormalButtonHashMap.get("Run as Guest").setCoordinates(4,6);
        LoginUI.NormalButtonHashMap.get("Run as Guest").translateObject(70,52);
        LoginUI.NormalButtonHashMap.get("Run as Guest").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LoginUI.GetStage().close();
            }
        });


        LoginUI.addButtonToTheGrid("Exit", 1,1);
        LoginUI.NormalButtonHashMap.get("Exit").setCoordinates(4,7);
        LoginUI.NormalButtonHashMap.get("Exit").translateObject(70,52);
        LoginUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });

        LoginUI.addButtonToTheGrid("?", 1,1);
        LoginUI.NormalButtonHashMap.get("?").setCoordinates(4,8);
        LoginUI.NormalButtonHashMap.get("?").translateObject(70,52);
        LoginUI.NormalButtonHashMap.get("?").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OpenHTMLWebsite();
            }
        });
        LoginUI.Configure2D();
        LoginUI.GetStage().showAndWait();
    }
    private void BuildingMenu(){

        //set up Stage

        NormalUserInterface FirstUI = new NormalUserInterface(650, 950, primaryStage);
        FirstUI.GetStage().setTitle("Building Menu");
        //Initial configurations
        FirstUI.createGridPane(250, 0, 2);
        FirstUI.addStageDimensions();
        FirstUI.addCameraAndSubscene();
        FirstUI.setSimulation();
        FirstUI.Configure();

        FirstUI.addText("Launch Statistics:", 30, 3, 1);
        FirstUI.addNormalDataToTheGrid("Total Rocket Mass", "kg");
        FirstUI.addNormalDataToTheGrid("Engine Thrust", "N");
        FirstUI.addNormalDataToTheGrid("Burn Rate", "kg/s");
        FirstUI.addNormalDataToTheGrid("Drag Coefficient", "none"); //can we do this as just drag?
        FirstUI.addNormalDataToTheGrid("Delta-V", "m/s");
        FirstUI.addNormalDataToTheGrid("Nose Radius", "m");
        FirstUI.addNormalDataToTheGrid("Specific Impulse", "s");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("X", "?");
        FirstUI.addNormalDataToTheGrid("Time Step", "s");
        FirstUI.addNormalDataToTheGrid("Playback Speed", "s/s");
        FirstUI.addNormalDataToTheGrid("Simulation Duration", "s");
        //FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(12345);

        FirstUI.addText("________________________", 20, 3, 1);
        FirstUI.addText("Edit:", 30, 3, 1);
        FirstUI.addButtonToTheGrid("Rocket", 1,1);
        FirstUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                RocketParameterMenu(primaryStage);
                FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(saved_Engine_Mass+saved_Payload_Mass+saved_Hull_Mass+saved_Fuel_Mass);
                FirstUI.NormalDataHashMap.get("Engine Thrust").setText(saved_Engine_Thrust);
                FirstUI.NormalDataHashMap.get("Burn Rate").setText(saved_Burn_Rate);
                FirstUI.NormalDataHashMap.get("Drag Coefficient").setText(saved_Drag_Coefficient);
                FirstUI.NormalDataHashMap.get("Delta-V").setText(CalculateDeltaV());
                FirstUI.NormalDataHashMap.get("Nose Radius").setText(saved_Nose_Diameter/2);
                FirstUI.NormalDataHashMap.get("Specific Impulse").setText(specificImpulse);

            }
        });
        FirstUI.addButtonToTheGrid("Environment", 1,1);
        FirstUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EnvironmentParameterMenu(primaryStage);
                FirstUI.NormalDataHashMap.get("Time Step").setText(saved_Time_Step);
                FirstUI.NormalDataHashMap.get("Playback Speed").setText(saved_Playback_Speed);
                FirstUI.NormalDataHashMap.get("Simulation Duration").setText(saved_Simulation_Duration);
            }
        });
        FirstUI.addButtonToTheGrid("Launch", 1,1);
        FirstUI.NormalButtonHashMap.get("Launch").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FirstUI.GetStage().close();
            }
        });
        FirstUI.addButtonToTheGrid("Exit", 1,1);
        FirstUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });

        FirstUI.GetStage().showAndWait();

    }
    private Rocket CalculateTrajectory(){
        Simulation sim = new Simulation();
        sim.run_simulation(inputs);
        environment = sim.Get_environment();
        return sim.getRocket();
    }
    private void SimulationMenu(Rocket theRocket) {
        System.out.println("Simulation Menu");
        TheAnimationWindow SecondUI = new TheAnimationWindow(700,1200, primaryStage, theRocket, playback_speed, environment);
        SecondUI.GetStage().setTitle("Simulation");

        SecondUI.createGridPane(250,0,2);
        SecondUI.addStageDimensions();
        SecondUI.addCameraAndSubscene();
        SecondUI.setSimulation();
        SecondUI.Configure();
        SecondUI.SetRocketPosition();
        SecondUI.SetCamera();
        //lights and large objects
        SecondUI.createSun();
        SecondUI.createGround();
        SecondUI.createAmbientLight();
        //movement
        SecondUI.movement();
        SecondUI.RocketMovement();


        SecondUI.addText("Real Time Data:", 30, 3, 1);
        SecondUI.addNormalDataToTheGrid("Time", "s");
        SecondUI.addNormalDataToTheGrid("Location", "km");
        SecondUI.addNormalDataToTheGrid("Velocity", "m/s");
        SecondUI.addNormalDataToTheGrid("Acceleration", "m/s^2");
        SecondUI.addNormalDataToTheGrid("Fuel", "kg");
        SecondUI.addNormalDataToTheGrid("Atmospheric Density", "kg/m^3"); // do later
        SecondUI.addNormalDataToTheGrid("Gravity", "kg/m^3"); // do later
        SecondUI.addNormalDataToTheGrid("Drag", "N"); // do later

        SecondUI.addText("________________________", 20, 3, 1);
        SecondUI.addText("Options:", 30, 3, 1);
        SecondUI.addButtonToTheGrid("Rocket", 3,1);
        SecondUI.NormalButtonHashMap.get("Rocket").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                RocketParameterMenu(primaryStage);
                SecondUI.refocus();
            }
        });
        SecondUI.addButtonToTheGrid("Environment", 3,1);
        SecondUI.NormalButtonHashMap.get("Environment").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EnvironmentParameterMenu(primaryStage);
                SecondUI.refocus();
            }
        });
        SecondUI.addButtonToTheGrid("Analysis", 3,1);
        SecondUI.NormalButtonHashMap.get("Analysis").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Analysis").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                SecondUI.refocus();
            }
        });
        SecondUI.addButtonToTheGrid("Restart", 3,1);
        SecondUI.NormalButtonHashMap.get("Restart").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Restart").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Restart").Gettext() + " was clicked");
                //restart here
            }
        });
        SecondUI.addButtonToTheGrid("Exit", 3,1);
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

    private void RocketParameterMenu(Stage primaryStage) {
        NormalUserInterface ThirdUI = new NormalUserInterface(450, 350, primaryStage);
        ThirdUI.GetStage().setTitle("Rocket Building Menu");
        ThirdUI.createGridPane(250,2,2);
        ThirdUI.addStageDimensions();

        ThirdUI.addText("Rocket:", 20, 3, 1);
        ThirdUI.addFieldToTheGrid("Fuel Mass","kg");
        ThirdUI.addFieldToTheGrid("Hull Mass","kg");
        ThirdUI.addFieldToTheGrid("Engine Mass","kg");
        ThirdUI.addFieldToTheGrid("Payload Mass","kg");
        ThirdUI.addFieldToTheGrid("Engine Thrust","N");
        ThirdUI.addFieldToTheGrid("Burn Rate","kg/s");
        ThirdUI.addFieldToTheGrid("Nose Diameter","m");
        ThirdUI.addFieldToTheGrid("Drag Coefficient"," ");

        ThirdUI.addButtonToTheGrid("LOAD",1,1);
        ThirdUI.NormalButtonHashMap.get("LOAD").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LoadingDatabase();
            }
        });
        ThirdUI.addButtonToTheGrid("SAVE",1,1);
        ThirdUI.addButtonToTheGrid("SET TO DEFAULT",1,1);
        ThirdUI.NormalButtonHashMap.get("SET TO DEFAULT").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setDefaultsRocket(ThirdUI);
            }
        });
        ThirdUI.addButtonToTheGrid("ACCEPT",1,1);
        ThirdUI.NormalButtonHashMap.get("ACCEPT").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                inputs.put("Fuel Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Fuel Mass").getValue()));
                inputs.put("Hull Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Hull Mass").getValue()));
                inputs.put("Engine Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Mass").getValue()));
                inputs.put("Payload Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Payload Mass").getValue()));
                inputs.put("Engine Thrust", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Thrust").getValue()));
                inputs.put("Burn Rate", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Burn Rate").getValue()));
                inputs.put("Nose Diameter", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Nose Diameter").getValue()));
                inputs.put("Drag Coefficient", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getValue()));


                saved_Fuel_Mass = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Fuel Mass").getValue());
                saved_Hull_Mass = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Hull Mass").getValue());
                saved_Engine_Mass = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Mass").getValue());
                saved_Payload_Mass = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Payload Mass").getValue());
                saved_Engine_Thrust = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Thrust").getValue());
                saved_Burn_Rate = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Burn Rate").getValue());
                saved_Nose_Diameter = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Nose Diameter").getValue());
                saved_Drag_Coefficient = Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getValue());

                ThirdUI.GetStage().close();
            }
        });

        setSavedDefaultsRocket(ThirdUI);
        ThirdUI.Configure2D();
        ThirdUI.GetStage().showAndWait();
    }
    private void setDefaultsRocket(NormalUserInterface UI){
        UI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText("500000.0");
        UI.NormalFieldHashMap.get("Hull Mass").getThing2().setText("82657.0");//91000kg
        UI.NormalFieldHashMap.get("Engine Mass").getThing2().setText("8343.0");
        UI.NormalFieldHashMap.get("Payload Mass").getThing2().setText("0.0");
        UI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText("12000000.0");
        UI.NormalFieldHashMap.get("Burn Rate").getThing2().setText("3937.0");
        UI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText("3.71");
        UI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText("0.8");
    } //default values in here
    private void setSavedDefaultsRocket(NormalUserInterface UI){
        if (saved_Fuel_Mass != 0){
            String x = Double.toString(saved_Fuel_Mass);
            UI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText("500000.0");
        }
        if (saved_Hull_Mass != 0){
            String x = Double.toString(saved_Hull_Mass);
            UI.NormalFieldHashMap.get("Hull Mass").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Hull Mass").getThing2().setText("82657.0");
        }
        if (saved_Engine_Mass != 0){
            String x = Double.toString(saved_Engine_Mass);
            UI.NormalFieldHashMap.get("Engine Mass").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Engine Mass").getThing2().setText("8343.0");
        }
        if (saved_Payload_Mass != 0){
            String x = Double.toString(saved_Payload_Mass);
            UI.NormalFieldHashMap.get("Payload Mass").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Payload Mass").getThing2().setText("0.0");
        }
        if (saved_Engine_Thrust != 0){
            String x = Double.toString(saved_Engine_Thrust);
            UI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText("12000000.0");
        }
        if (saved_Burn_Rate != 0){
            String x = Double.toString(saved_Burn_Rate);
            UI.NormalFieldHashMap.get("Burn Rate").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Burn Rate").getThing2().setText("3937.0");
        }
        if (saved_Nose_Diameter != 0){
            String x = Double.toString(saved_Nose_Diameter);
            UI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText("3.71");
        }
        if (saved_Drag_Coefficient != 0){
            String x = Double.toString(saved_Drag_Coefficient);
            UI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText("0.8");
        }
    }//and in here

    private void EnvironmentParameterMenu(Stage primaryStage) {
        NormalUserInterface FourthUI = new NormalUserInterface(540, 350, primaryStage);
        FourthUI.GetStage().setTitle("Environment Building Menu");
        FourthUI.createGridPane(250,2,2);
        FourthUI.addStageDimensions();

        FourthUI.addText("Simulation:", 20, 3, 1);
        FourthUI.addFieldToTheGrid("Time Step","s");
        FourthUI.addFieldToTheGrid("Playback Speed","s/s");
        FourthUI.addFieldToTheGrid("Simulation Duration","s");
        FourthUI.addText("Wind:", 20, 3, 1);
        FourthUI.addFieldToTheGrid("Wind Speed","m/s");
        FourthUI.addFieldToTheGrid("Wind Angle","*");
        FourthUI.addText("Orientation:", 20, 3, 1);
        FourthUI.addFieldToTheGrid("Altitude","y-axis");
        FourthUI.addFieldToTheGrid("Azimuth","x-axis");
        FourthUI.addText("Position:", 20, 3, 1);
        FourthUI.addFieldToTheGrid("Latitude","*");
        FourthUI.addFieldToTheGrid("Longitude","*");

        FourthUI.addButtonToTheGrid("LOAD",1,1);
        FourthUI.addButtonToTheGrid("SAVE",1,1);
        FourthUI.addButtonToTheGrid("SET TO DEFAULT",1,1);
        FourthUI.NormalButtonHashMap.get("SET TO DEFAULT").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setDefaultsEnvironment(FourthUI);
            }
        });
        FourthUI.addButtonToTheGrid("ACCEPT",1,1);
        FourthUI.NormalButtonHashMap.get("ACCEPT").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                inputs.put("Time Step", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Time Step").getValue()));
                playback_speed = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Playback Speed").getValue());
                inputs.put("Simulation Duration", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Simulation Duration").getValue()));
                inputs.put("Wind Speed", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Speed").getValue()));
                inputs.put("Wind Angle", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Angle").getValue()));
                inputs.put("Altitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Altitude").getValue()));
                inputs.put("Azimuth", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Azimuth").getValue()));
                inputs.put("Latitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Latitude").getValue()));
                inputs.put("Longitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Longitude").getValue()));

                saved_Time_Step = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Time Step").getValue());
                saved_Playback_Speed = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Playback Speed").getValue());
                saved_Simulation_Duration = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Simulation Duration").getValue());
                saved_Wind_Speed = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Speed").getValue());
                saved_Wind_Angle = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Angle").getValue());
                saved_Altitude = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Altitude").getValue());
                saved_Azimuth = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Azimuth").getValue());
                saved_Latitude = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Latitude").getValue());
                saved_Longitude = Double.parseDouble(FourthUI.NormalFieldHashMap.get("Longitude").getValue());

                FourthUI.GetStage().close();
            }
        });

        setSavedDefaultsEnvironment(FourthUI);
        FourthUI.Configure2D();
        FourthUI.GetStage().showAndWait();

    }
    private void setDefaultsEnvironment(NormalUserInterface UI){
        UI.NormalFieldHashMap.get("Time Step").getThing2().setText("0.01");
        UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText("5.0");
        UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText("6000.0");;
        UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText("0.0");
        UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText("0.0");
        UI.NormalFieldHashMap.get("Altitude").getThing2().setText("90.0");
        UI.NormalFieldHashMap.get("Azimuth").getThing2().setText("0.0");
        UI.NormalFieldHashMap.get("Latitude").getThing2().setText("0.0");
        UI.NormalFieldHashMap.get("Longitude").getThing2().setText("0.0");
    } //default values in here
    private void setSavedDefaultsEnvironment(NormalUserInterface UI){

        if (saved_Time_Step != 0){
            String x = Double.toString(saved_Time_Step);
            UI.NormalFieldHashMap.get("Time Step").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Time Step").getThing2().setText("0.01");
        }
        if (saved_Playback_Speed != 0){
            String x = Double.toString(saved_Playback_Speed);
            UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText("5.0");
        }
        if (saved_Simulation_Duration != 0){
            String x = Double.toString(saved_Simulation_Duration);
            UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText("6000.0");
        }
        if (saved_Wind_Speed!= 0){
            String x = Double.toString(saved_Wind_Speed);
            UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText("0.0");
        }
        if (saved_Wind_Angle != 0){
            String x = Double.toString(saved_Wind_Angle);
            UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText("0.0");
        }
        if (saved_Altitude != 0){
            String x = Double.toString(saved_Altitude);
            UI.NormalFieldHashMap.get("Altitude").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Altitude").getThing2().setText("90.0");
        }
        if (saved_Azimuth != 0){
            String x = Double.toString(saved_Azimuth);
            UI.NormalFieldHashMap.get("Azimuth").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Azimuth").getThing2().setText("0.0");
        }
        if (saved_Latitude != 0){
            String x = Double.toString(saved_Latitude);
            UI.NormalFieldHashMap.get("Latitude").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Latitude").getThing2().setText("0.0");
        }
        if (saved_Longitude != 0){
            String x = Double.toString(saved_Longitude);
            UI.NormalFieldHashMap.get("Longitude").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Longitude").getThing2().setText("0.0");
        }
    } //and in here

    private void LoadingDatabase(){
        //change to be in the Normal User Interface
        TreeItem rootItem = new TreeItem("Load");

        TreeItem webItem = new TreeItem("Robin Pound");
        webItem.getChildren().add(new TreeItem("Environment: Light wind, north pole"));
        webItem.getChildren().add(new TreeItem("Environment: Clear day, UK London"));
        webItem.getChildren().add(new TreeItem("Environment: Clear day, RUSSIA Moscow"));
        webItem.getChildren().add(new TreeItem("Rocket: Falcon 9"));
        rootItem.getChildren().add(webItem);

        TreeItem javaItem = new TreeItem("Sam Kelly");
        javaItem.getChildren().add(new TreeItem("Environment: Strong wind, CHINA Beijing"));
        javaItem.getChildren().add(new TreeItem("Rocket: Saturn V"));
        javaItem.getChildren().add(new TreeItem("Rocket: Soyuz"));
        rootItem.getChildren().add(javaItem);

        TreeView treeView = new TreeView();
        treeView.setRoot(rootItem);

        treeView.setShowRoot(false);
        VBox vbox = new VBox(treeView);

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);

        primaryStage.show();
    }
    private void SavingLogToDatabase(){

    }
    private void OpenHTMLWebsite(){
        File f = new File ("src/code/HelpPage.html");
        try{
            Desktop.getDesktop().browse(f.toURI());
        }catch(IOException e){
            System.out.println(e);
        }

    }
    private double CalculateDeltaV(){ //https://en.wikipedia.org/wiki/Tsiolkovsky_rocket_equation
        double mf = saved_Engine_Mass + saved_Hull_Mass + saved_Payload_Mass;
        double m0 = mf + saved_Fuel_Mass;
        double gravity = 9.798;
        double totalImpulse = saved_Engine_Thrust * (saved_Fuel_Mass/saved_Burn_Rate);
        specificImpulse = totalImpulse / (m0 * gravity);
        System.out.println(specificImpulse);
        double ve = specificImpulse * m0 * gravity;

        double DeltaV = ve * Math.log(m0/mf);
        return DeltaV;
    }

    public static void main(String[] args) {
        launch(args);
    }

}



