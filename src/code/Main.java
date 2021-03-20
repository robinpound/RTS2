package code;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Boolean CloseClicked = false; //need to use to close windows using X
    private HashMap<String, Double> inputs = new HashMap<String, Double>();
    private Stage primaryStage;
    private double playback_speed;
    private double specificImpulse;
    private Environment environment;
    private Rocket theRocket;
    private String xaxis;
    private String yaxis;


    //turn to hashmaps(rocket and environment). set default falues list/arraylist.

    private HashMap<String, Double> saved_rocket = new HashMap<>();
    private HashMap<String, Double> saved_environment = new HashMap<>();
    Map<String, Double> default_values = new HashMap<String, Double>() {{
        put("Fuel Mass",500000.0);
        put("Hull Mass",82657.0);
        put("Engine Mass",8343.0);
        put("Payload Mass",0.0);
        put("Engine Thrust",20000000.0);
        put("Burn Rate",3937.0);
        put("Nose Diameter",3.71);
        put("Drag Coefficient",0.8);

        put("Time Step",0.01);
        put("Playback Speed",50.0);
        put("Simulation Duration",10000.0);
        put("Wind Speed",3.0);
        put("Wind Angle",45.0);
        put("Altitude",87.0);
        put("Azimuth",0.0);
        put("Latitude",42.0);
        put("Longitude",-1.0);
    }};


    //Database
    private Database database = new Database("jdbc:sqlite:RocketTrajectorySimulatorDatabase.db");
    String saved_username = "Guest";


    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        LoginMenu();
        BuildingMenu();
        CalculateTrajectory();
        SimulationMenu(); //make theRocket member of the class (private)
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
                RocketParameterMenu();
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
                EnvironmentParameterMenu();

                FirstUI.NormalDataHashMap.get("Time Step").setText(saved_environment.get("saved_Time_Step"));
                FirstUI.NormalDataHashMap.get("Playback Speed").setText(saved_environment.get("saved_Playback_Speed"));
                FirstUI.NormalDataHashMap.get("Simulation Duration").setText(saved_environment.get("saved_Simulation_Duration"));
            }
        });
        FirstUI.addButtonToTheGrid("Launch", 1,1);
        FirstUI.NormalButtonHashMap.get("Launch").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (saved_rocket.size() != 0 && saved_environment.size() != 0){
                    FirstUI.GetStage().close();
                }
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
    private void CalculateTrajectory(){
        Simulation sim = new Simulation();
        sim.run_simulation(inputs);
        environment = sim.Get_environment();
        theRocket = sim.getRocket();
    }
    private void SimulationMenu(){
        System.out.println("Simulation Menu");
        System.out.println(playback_speed);
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
        SecondUI.addNormalDataToTheGrid("Altitude", "km");
        SecondUI.addNormalDataToTheGrid("Velocity", "m/s");
        SecondUI.addNormalDataToTheGrid("Acceleration", "m/s^2");
        SecondUI.addNormalDataToTheGrid("Fuel", "kg");
        SecondUI.addProgressBar(0);
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
                RocketParameterMenu();
                SecondUI.refocus();
            }
        });
        SecondUI.addButtonToTheGrid("Environment", 3,1);
        SecondUI.NormalButtonHashMap.get("Environment").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EnvironmentParameterMenu();
                SecondUI.refocus();
            }
        });
        SecondUI.addButtonToTheGrid("Analysis", 3,1);
        SecondUI.NormalButtonHashMap.get("Analysis").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Analysis").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnalysisMenu();
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

    private void RocketParameterMenu() {
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
                HashMap<String, Double> returningrocketinputs = database.getRocketRecord(primaryStage);
                ThirdUI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText(returningrocketinputs.get("fuelmass").toString());
                ThirdUI.NormalFieldHashMap.get("Hull Mass").getThing2().setText(returningrocketinputs.get("hullmass").toString());
                ThirdUI.NormalFieldHashMap.get("Engine Mass").getThing2().setText(returningrocketinputs.get("enginemass").toString());
                ThirdUI.NormalFieldHashMap.get("Payload Mass").getThing2().setText(returningrocketinputs.get("payloadmass").toString());
                ThirdUI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText(returningrocketinputs.get("enginethrust").toString());
                ThirdUI.NormalFieldHashMap.get("Burn Rate").getThing2().setText(returningrocketinputs.get("burnrate").toString());
                ThirdUI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText(returningrocketinputs.get("nosediameter").toString());
                ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText(returningrocketinputs.get("dragcoefficient").toString());
            }
        });
        ThirdUI.addButtonToTheGrid("SAVE AS",1,1);
        ThirdUI.NormalButtonHashMap.get("SAVE AS").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                database.insertRocketRecord(primaryStage, ThirdUI.NormalFieldHashMap,saved_username);
            }
        });
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
    }
    private void setSavedDefaultsRocketLoop(NormalUserInterface UI, String savedname, String fieldname){
        if (saved_rocket.get(savedname) != null){
            String x = Double.toString(saved_rocket.get(savedname));
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(default_values.get(fieldname).toString());
            saved_rocket.put(savedname, default_values.get(fieldname));
        }
    }
    private void setSavedDefaultsRocket(NormalUserInterface UI){
        setSavedDefaultsRocketLoop(UI,"saved_Fuel_Mass", "Fuel Mass");
        setSavedDefaultsRocketLoop(UI,"saved_Hull_Mass", "Hull Mass");
        setSavedDefaultsRocketLoop(UI,"saved_Engine_Mass", "Engine Mass");
        setSavedDefaultsRocketLoop(UI,"saved_Payload_Mass", "Payload Mass");
        setSavedDefaultsRocketLoop(UI,"saved_Engine_Thrust", "Engine Thrust");
        setSavedDefaultsRocketLoop(UI,"saved_Burn_Rate", "Burn Rate");
        setSavedDefaultsRocketLoop(UI,"saved_Nose_Diameter", "Nose Diameter");
        setSavedDefaultsRocketLoop(UI,"saved_Drag_Coefficient", "Drag Coefficient");
    }

    private void EnvironmentParameterMenu() {
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
        UI.NormalFieldHashMap.get("Time Step").getThing2().setText(default_values.get("Time Step").toString());
        UI.NormalFieldHashMap.get("Playback Speed").getThing2().setText(default_values.get("Playback Speed").toString());
        UI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText(default_values.get("Simulation Duration").toString());;
        UI.NormalFieldHashMap.get("Wind Speed").getThing2().setText(default_values.get("Wind Speed").toString());
        UI.NormalFieldHashMap.get("Wind Angle").getThing2().setText(default_values.get("Wind Angle").toString());
        UI.NormalFieldHashMap.get("Altitude").getThing2().setText(default_values.get("Altitude").toString());
        UI.NormalFieldHashMap.get("Azimuth").getThing2().setText(default_values.get("Azimuth").toString());
        UI.NormalFieldHashMap.get("Latitude").getThing2().setText(default_values.get("Latitude").toString());
        UI.NormalFieldHashMap.get("Longitude").getThing2().setText(default_values.get("Longitude").toString());
    }
    private void setSavedDefaultsEnvironmentLoop(NormalUserInterface UI, String savedname, String fieldname){
        if (saved_environment.get(savedname) != null){
            String x = Double.toString(saved_environment.get(savedname));
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(x);
        }else{
            UI.NormalFieldHashMap.get(fieldname).getThing2().setText(default_values.get(fieldname).toString());
            saved_environment.put(savedname, default_values.get(fieldname));
        }
    }
    private void setSavedDefaultsEnvironment(NormalUserInterface UI){
        setSavedDefaultsEnvironmentLoop(UI,"saved_Time_Step", "Time Step");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Playback_Speed", "Playback Speed");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Simulation_Duration", "Simulation Duration");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Wind_Speed", "Wind Speed");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Wind_Angle", "Wind Angle");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Altitude", "Altitude");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Azimuth", "Azimuth");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Latitude", "Latitude");
        setSavedDefaultsEnvironmentLoop(UI,"saved_Longitude", "Longitude");
    }

    private void AnalysisMenu(){
        NormalUserInterface GraphUI = new NormalUserInterface(600,800, primaryStage);
        GraphUI.GetStage().setTitle("Analysis");
        GraphUI.createGridPane(250,2,2);
        GraphUI.addStageDimensions();

        GraphUI.addDropDown("x-axis",0);
        GraphUI.addDropDown("y-axis",0);

        GraphUI.addButtonToTheGrid("Generate",1,1);
        GraphUI.addProgressBar(0);
        GraphUI.NormalButtonHashMap.get("Generate").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GraphUI.NormalDropDownMenuHashMap.get("x-axis").getValue() != null && GraphUI.NormalDropDownMenuHashMap.get("y-axis").getValue().toString() != null){
                    HashMap<String, Integer> indexforvalue = new HashMap<String, Integer>() {{
                        put("Time Elapsed", 0);
                        put("Fuel Mass", 1);
                        put("Atmospheric Density", 26);
                        put("Altitude", -1);
                        put("PositionX", 2);put("PositionY", 3);put("PositionZ", 4);
                        put("OrientationX", 5);put("OrientationY", 6);put("OrientationZ", 7);
                        put("VelocityX", 8);put("VelocityY", 9);put("VelocityZ", 10);
                        put("AccelerationX", 11);put("AccelerationY", 12);put("AccelerationZ", 13);
                        put("ThrustX", 14);put("ThrustY", 15);put("ThrustZ", 16);
                        put("DragX", 17);put("DragY", 18);put("DragZ", 19);
                        put("WindX", 20);put("WindY", 21);put("WindZ", 22);
                        put("GravityX", 23);put("GravityY", 24);put("GravityZ", 25);
                        put("Velocity Magnitude", -2);put("Acceleration Magnitude", -3);put("Drag Magnitude", -4);put("Thrust Magnitude", -5);put("Gravity Magnitude", -6);
                    }};
                    xaxis = GraphUI.NormalDropDownMenuHashMap.get("x-axis").getValue().toString();
                    yaxis = GraphUI.NormalDropDownMenuHashMap.get("y-axis").getValue().toString();
                    System.out.println(xaxis + yaxis);
                    GraphUI.getScatterChart();

                    NumberAxis xAxis = new NumberAxis();
                    xAxis.setLabel(xaxis);
                    NumberAxis yAxis = new NumberAxis();
                    yAxis.setLabel(yaxis);

                    XYChart.Series dataSeries1 = new XYChart.Series();
                    dataSeries1.setName(saved_username+"'s data");


                    for (int i = 0; i < theRocket.get_Arraylist().size(); i+=1+theRocket.get_Arraylist().size()/10000){
                        //System.out.println(i + "/" + theRocket.get_Arraylist().size());
                        GraphUI.progressBar.setProgress(i/(float)theRocket.get_Arraylist().size());
                        double xaxisplot= getplot(indexforvalue,i,xaxis);
                        double yaxisplot= getplot(indexforvalue,i,yaxis);
                        dataSeries1.getData().add(new XYChart.Data( xaxisplot, yaxisplot));

                    }
                    System.out.println(theRocket.get_Arraylist().size());

                    GraphUI.addgraph(0, xAxis, yAxis,dataSeries1);
                }
            }
        });
        GraphUI.addButtonToTheGrid("Export",1,1);
        GraphUI.NormalButtonHashMap.get("Export").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                java.util.List<String> columnnames = new ArrayList<String>();
                columnnames.add("Time");
                columnnames.add("Fuel Mass");
                columnnames.add("PositionX");columnnames.add("PositionX");columnnames.add("PositionZ");
                columnnames.add("OrientationX");columnnames.add("OrientationY");columnnames.add("OrientationZ");
                columnnames.add("VelocityX");columnnames.add("VelocityY");columnnames.add("VelocityX");
                columnnames.add("AccelerationX");columnnames.add("AccelerationY");columnnames.add("AccelerationZ");
                columnnames.add("ThrustX");columnnames.add("ThrustY");columnnames.add("ThrustZ");
                columnnames.add("DragX");columnnames.add("DragY");columnnames.add("DragZ");
                columnnames.add("WindX");columnnames.add("WindY");columnnames.add("WindZ");
                columnnames.add("GravityX");columnnames.add("GravityY");columnnames.add("GravityZ");
                columnnames.add("Atmospheric Density");
                saveascsv(columnnames, theRocket.get_Arraylist(), "exportedfile.csv");
            }
        });
        GraphUI.Configure2D();
        GraphUI.GetStage().showAndWait();
    }
    private void saveascsv(java.util.List<String> columnnames, java.util.List<java.util.List<Double>> arraylist, String csvfile){
        try {
            FileWriter filewriter = new FileWriter(csvfile);
            CSVPrinter csvPrinter = new CSVPrinter(filewriter, CSVFormat.DEFAULT.withHeader((ResultSet) columnnames));
            csvPrinter.printRecords(arraylist);
            csvPrinter.flush();
            csvPrinter.close();
        } catch (Exception e) {
            System.out.println("Error writing to CSV file: "+e.toString());
        }
    }
    private Double getplot(HashMap<String, Integer> indexforvalue, int i, String axis){
        double axisplot = 0;
        if(indexforvalue.get(axis) >= 0) {
            axisplot = theRocket.get_Arraylist().get(i).get(indexforvalue.get(axis));

        }else if(indexforvalue.get(axis) < 0 ){
            if (indexforvalue.get(axis) == -1){ //Altitude
                double positionx = theRocket.get_Arraylist().get(i).get(2);
                double positiony = theRocket.get_Arraylist().get(i).get(3);
                double positionz = theRocket.get_Arraylist().get(i).get(4);
                axisplot = environment.get_Altitude(new Vector3(positionx,positiony,positionz));
            }else if(indexforvalue.get(axis) == -2){ //Velocity Magnitude
                double velocityX = theRocket.get_Arraylist().get(i).get(8);
                double velocityY = theRocket.get_Arraylist().get(i).get(9);
                double velocityZ = theRocket.get_Arraylist().get(i).get(10);
                axisplot = Math.sqrt(velocityX*velocityX+velocityY*velocityY+velocityZ*velocityZ);
            }else if(indexforvalue.get(axis) == -3){ //Acceleration Magnitude
                double AccelerationX = theRocket.get_Arraylist().get(i).get(11);
                double AccelerationY = theRocket.get_Arraylist().get(i).get(12);
                double AccelerationZ = theRocket.get_Arraylist().get(i).get(13);
                axisplot = Math.sqrt(AccelerationX*AccelerationX+AccelerationY*AccelerationY+AccelerationZ*AccelerationZ);
            }else if(indexforvalue.get(axis) == -4){//Drag Magnitude
                double DragX = theRocket.get_Arraylist().get(i).get(17);
                double DragY = theRocket.get_Arraylist().get(i).get(18);
                double DragZ = theRocket.get_Arraylist().get(i).get(19);
                axisplot = Math.sqrt(DragX*DragX+DragY*DragY+DragZ*DragZ);
            }else if(indexforvalue.get(axis) == -5){//Thrust Magnitude
                double ThrustX = theRocket.get_Arraylist().get(i).get(14);
                double ThrustY = theRocket.get_Arraylist().get(i).get(15);
                double ThrustZ = theRocket.get_Arraylist().get(i).get(16);
                axisplot = Math.sqrt(ThrustX*ThrustX+ThrustY*ThrustY+ThrustZ*ThrustZ);
            }else if(indexforvalue.get(axis) == -6) {//Gravity Magnitude
                double GravX = theRocket.get_Arraylist().get(i).get(23);
                double GravY = theRocket.get_Arraylist().get(i).get(24);
                double GravZ = theRocket.get_Arraylist().get(i).get(25);
                axisplot = Math.sqrt(GravX*GravX+GravY*GravY+GravZ*GravZ);
            }
        }
        return axisplot;
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