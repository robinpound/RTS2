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
import java.util.Map;

public class Main extends Application {

    private Boolean CloseClicked = false; //need to use to close windows using X
    private HashMap<String, Double> inputs = new HashMap<String, Double>();
    private Stage primaryStage;
    private double playback_speed;
    private Environment environment;
    private double specificImpulse;


    //turn to hashmaps(rocket and environment). set default falues list/arraylist.

    private HashMap<String, Double> saved_rocket = new HashMap<>();
    private HashMap<String, Double> saved_environment = new HashMap<>();
    Map<String, Double> default_values = new HashMap<String, Double>() {{
        put("Fuel Mass",500000.0);
        put("Hull Mass",82657.0);
        put("Engine Mass",8343.0);
        put("Payload Mass",0.0);
        put("Engine Thrust",12000000.0);
        put("Burn Rate",3937.0);
        put("Nose Diameter",3.71);
        put("Drag Coefficient",0.8);
        put("i",5.5);
        put("j",5.5);
        put("k",5.5);
        put("l",5.5);
        put("m",5.5);
        put("n",5.5);
        put("a2",5.5);
        put("b2",5.5);
        put("c2",5.5);
        put("d2",5.5);
        put("e2",5.5);
        put("f2",5.5);
        put("g2",5.5);
        put("h2",5.5);
        put("i2",5.5);
        put("j2",5.5);
        put("k2",5.5);
        put("l2",5.5);
        put("m2",5.5);
        put("n2",5.5);
    }};

    //Database
    private Database database = new Database("jdbc:sqlite:RocketTrajectorySimulatorDatabase.db");
    String saved_username = "Guest";


    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        LoginMenu();
        BuildingMenu();
        Rocket theRocket = CalculateTrajectory();
        SimulationMenu(theRocket); //make theRocket member of the class (private)
        closeDatabase();
    }

    private void LoginMenu(){
        NormalUserInterface LoginUI = new NormalUserInterface(500, 650, primaryStage);
        LoginUI.GetStage().setTitle("3D Rocket Trajectory Simulator - 2");
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
        LoginUI.NormalFieldHashMap.get("Password").setCoordinatespassword(0,4);
        LoginUI.addButtonToTheGrid("LOGIN",1,1);
        LoginUI.NormalButtonHashMap.get("LOGIN").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String username = LoginUI.NormalFieldHashMap.get("Username").getThing2().getText();
                String password = LoginUI.NormalFieldHashMap.get("Password").getThing4().getText();
                System.out.println(password);
                Boolean result = database.LoginUserCheck(username, password);
                System.out.println(result);
                if (result){ //the continue "if"
                    saved_username = username;
                    LoginUI.GetStage().close();
                }
            }
        });

        LoginUI.addCoordinateText("Register:", 25,REGISTERCOLUMN,REGISTERROW,1,1);

        LoginUI.addFieldToTheGrid("New Username"," ");
        LoginUI.NormalFieldHashMap.get("New Username").setCoordinates(REGISTERCOLUMN, REGISTERROW+1);

        LoginUI.addFieldToTheGrid("New Password", " ");
        LoginUI.NormalFieldHashMap.get("New Password").setCoordinatespassword(REGISTERCOLUMN,REGISTERROW+2);

        LoginUI.addFieldToTheGrid("Re-type New Password"," ");
        LoginUI.NormalFieldHashMap.get("Re-type New Password").setCoordinatespassword(REGISTERCOLUMN,REGISTERROW+3);

        LoginUI.addButtonToTheGrid("REGISTER",1,1);
        LoginUI.NormalButtonHashMap.get("REGISTER").setCoordinates(REGISTERCOLUMN, REGISTERROW+4);
        LoginUI.NormalButtonHashMap.get("REGISTER").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String username = LoginUI.NormalFieldHashMap.get("New Username").getThing2().getText();
                String password1 = LoginUI.NormalFieldHashMap.get("New Password").getThing4().getText();
                String password2 = LoginUI.NormalFieldHashMap.get("Re-type New Password").getThing4().getText();
                Boolean result = database.RegisterUser(username,password1,password2);
                if (result){
                    saved_username = username;
                    LoginUI.GetStage().close();
                }
            }
        });
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
                closeDatabase();
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
                double TotalRocketMass = saved_rocket.get("saved_Engine_Mass")+saved_rocket.get("saved_Payload_Mass")+saved_rocket.get("saved_Hull_Mass")+saved_rocket.get("saved_Fuel_Mass");
                FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(TotalRocketMass);
                FirstUI.NormalDataHashMap.get("Engine Thrust").setText(saved_rocket.get("saved_Engine_Thrust"));
                FirstUI.NormalDataHashMap.get("Burn Rate").setText(saved_rocket.get("saved_Burn_Rate"));
                FirstUI.NormalDataHashMap.get("Drag Coefficient").setText(saved_rocket.get("saved_Drag_Coefficient"));
                FirstUI.NormalDataHashMap.get("Delta-V").setText(CalculateDeltaV());
                FirstUI.NormalDataHashMap.get("Nose Radius").setText(saved_rocket.get("saved_Nose_Diameter")/2);
                FirstUI.NormalDataHashMap.get("Specific Impulse").setText(specificImpulse);

            }
        });
        FirstUI.addButtonToTheGrid("Environment", 1,1);
        FirstUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EnvironmentParameterMenu(primaryStage);

                FirstUI.NormalDataHashMap.get("Time Step").setText(saved_environment.get("saved_Time_Step"));
                FirstUI.NormalDataHashMap.get("Playback Speed").setText(saved_environment.get("saved_Playback_Speed"));
                FirstUI.NormalDataHashMap.get("Simulation Duration").setText(saved_environment.get("saved_Simulation_Duration"));
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
                closeDatabase();
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
    private void SimulationMenu(Rocket theRocket){
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
                closeDatabase();
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
                //put into building menu
                inputs.put("Fuel Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Fuel Mass").getValue()));
                inputs.put("Hull Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Hull Mass").getValue()));
                inputs.put("Engine Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Mass").getValue()));
                inputs.put("Payload Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Payload Mass").getValue()));
                inputs.put("Engine Thrust", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Thrust").getValue()));
                inputs.put("Burn Rate", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Burn Rate").getValue()));
                inputs.put("Nose Diameter", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Nose Diameter").getValue()));
                inputs.put("Drag Coefficient", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getValue()));
                //save parameters for later changes
                saved_rocket.put("saved_Fuel_Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Fuel Mass").getValue()));
                saved_rocket.put("saved_Hull_Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Hull Mass").getValue()));
                saved_rocket.put("saved_Engine_Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Mass").getValue()));
                saved_rocket.put("saved_Payload_Mass", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Payload Mass").getValue()));
                saved_rocket.put("saved_Engine_Thrust", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Engine Thrust").getValue()));
                saved_rocket.put("saved_Burn_Rate", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Burn Rate").getValue()));
                saved_rocket.put("saved_Nose_Diameter", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Nose Diameter").getValue()));
                saved_rocket.put("saved_Drag_Coefficient", Double.parseDouble(ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getValue()));

                ThirdUI.GetStage().close();
            }
        });

        setSavedDefaultsRocket(ThirdUI);
        ThirdUI.Configure2D();
        ThirdUI.GetStage().showAndWait();
    }
    private void setDefaultsRocket(NormalUserInterface UI){
        UI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText(default_values.get("Fuel Mass").toString());
        UI.NormalFieldHashMap.get("Hull Mass").getThing2().setText(default_values.get("Hull Mass").toString());
        UI.NormalFieldHashMap.get("Engine Mass").getThing2().setText(default_values.get("Engine Mass").toString());
        UI.NormalFieldHashMap.get("Payload Mass").getThing2().setText(default_values.get("Payload Mass").toString());
        UI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText(default_values.get("Engine Thrust").toString());
        UI.NormalFieldHashMap.get("Burn Rate").getThing2().setText(default_values.get("Burn Rate").toString());
        UI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText(default_values.get("Nose Diameter").toString());
        UI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText(default_values.get("Drag Coefficient").toString());
    } //default values in here
    private void setSavedDefaultsRocketloop(NormalUserInterface UI, String savedname, String fieldname){
        if (saved_rocket.get(savedname) != null){
            String x = Double.toString(saved_rocket.get(savedname));
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(default_values.get(fieldname).toString());
            saved_rocket.put(savedname, default_values.get(fieldname));
        }
    }
    private void setSavedDefaultsRocket(NormalUserInterface UI){
        setSavedDefaultsRocketloop(UI,"saved_Fuel_Mass", "Fuel Mass");
        setSavedDefaultsRocketloop(UI,"saved_Hull_Mass", "Hull Mass");
        setSavedDefaultsRocketloop(UI,"saved_Engine_Mass", "Engine Mass");
        setSavedDefaultsRocketloop(UI,"saved_Payload_Mass", "Payload Mass");
        setSavedDefaultsRocketloop(UI,"saved_Engine_Thrust", "Engine Thrust");
        setSavedDefaultsRocketloop(UI,"saved_Burn_Rate", "Burn Rate");
        setSavedDefaultsRocketloop(UI,"saved_Nose_Diameter", "Nose Diameter");
        setSavedDefaultsRocketloop(UI,"saved_Drag_Coefficient", "Drag Coefficient");
    }

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

                saved_environment.put("saved_Time_Step", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Time Step").getValue()));
                saved_environment.put("saved_Playback_Speed", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Playback Speed").getValue()));
                saved_environment.put("saved_Simulation_Duration", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Simulation Duration").getValue()));
                saved_environment.put("saved_Wind_Speed", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Speed").getValue()));
                saved_environment.put("saved_Wind_Angle", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Angle").getValue()));
                saved_environment.put("saved_Altitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Altitude").getValue()));
                saved_environment.put("saved_Azimuth", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Azimuth").getValue()));
                saved_environment.put("saved_Latitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Latitude").getValue()));
                saved_environment.put("saved_Longitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Longitude").getValue()));

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

        if (saved_environment.get("saved_Time_Step") != null){
            String x = Double.toString(saved_environment.get("saved_Time_Step"));
            UI.NormalFieldHashMap.get("Time Step").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Time Step").getThing2().setText("0.01");
        }
        if (saved_environment.get("saved_Playback_Speed") != null){
            String x = Double.toString(saved_environment.get("saved_Playback_Speed"));
            UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText("5.0");
        }
        if (saved_environment.get("saved_Simulation_Duration") != null){
            String x = Double.toString(saved_environment.get("saved_Simulation_Duration"));
            UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText("6000.0");
        }
        if (saved_environment.get("saved_Wind_Speed")!= null){
            String x = Double.toString(saved_environment.get("saved_Wind_Speed"));
            UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText("0.0");
        }
        if (saved_environment.get("saved_Wind_Angle") != null){
            String x = Double.toString(saved_environment.get("saved_Wind_Angle"));
            UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText("0.0");
        }
        if (saved_environment.get("saved_Altitude") != null){
            String x = Double.toString(saved_environment.get("saved_Altitude"));
            UI.NormalFieldHashMap.get("Altitude").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Altitude").getThing2().setText("90.0");
        }
        if (saved_environment.get("saved_Azimuth") != null){
            String x = Double.toString(saved_environment.get("saved_Azimuth"));
            UI.NormalFieldHashMap.get("Azimuth").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Azimuth").getThing2().setText("0.0");
        }
        if (saved_environment.get("saved_Latitude") != null){
            String x = Double.toString(saved_environment.get("saved_Latitude"));
            UI.NormalFieldHashMap.get("Latitude").getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get("Latitude").getThing2().setText("0.0");
        }
        if (saved_environment.get("saved_Longitude") != null){
            String x = Double.toString(saved_environment.get("saved_Longitude"));
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
        double mf = saved_rocket.get("saved_Engine_Mass") + saved_rocket.get("saved_Hull_Mass") + saved_rocket.get("saved_Payload_Mass");
        double m0 = mf + saved_rocket.get("saved_Fuel_Mass");
        double gravity = 9.798;
        double totalImpulse = saved_rocket.get("saved_Engine_Thrust") * (saved_rocket.get("saved_Fuel_Mass")/saved_rocket.get("saved_Burn_Rate"));
        specificImpulse = totalImpulse / (m0 * gravity);
        System.out.println(specificImpulse);
        double ve = specificImpulse * m0 * gravity;

        double DeltaV = ve * Math.log(m0/mf);
        return DeltaV;
    }

    private void closeDatabase(){
        database.closeDatabase();
    }
    public static void main(String[] args) {
        launch(args);
    }
}





