package code;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Formattable;
import java.util.HashMap;

public class Main extends Application {

    private Boolean CloseClicked = false; //need to use to close windows using X
    private HashMap<String, Double> inputs = new HashMap<String, Double>();
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        LoginMenu();
        BuildingMenu();
        Rocket theRocket = CalculateTrajectory();
        SimulationMenu(theRocket);//make theRocket member of the class (private)
    }
    private void LoginMenu(){
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

        LoginUI.addButtonToTheGrid("Run as Guest", 1,1);
        LoginUI.NormalButtonHashMap.get("Run as Guest").setCoordinates(4,6);
        LoginUI.NormalButtonHashMap.get("Run as Guest").translateObject(100,52);
        LoginUI.NormalButtonHashMap.get("Run as Guest").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LoginUI.GetStage().close();
            }
        });


        LoginUI.addButtonToTheGrid("Exit", 1,1);
        LoginUI.NormalButtonHashMap.get("Exit").setCoordinates(4,7);
        LoginUI.NormalButtonHashMap.get("Exit").translateObject(100,52);
        LoginUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
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
        FirstUI.addNormalDataToTheGrid("Delta-V", "m/s");
        FirstUI.addNormalDataToTheGrid("Burn_rate", "kg/s");
        FirstUI.addNormalDataToTheGrid("Drag-coefficient", "none"); //can we do this as just drag?
        FirstUI.addNormalDataToTheGrid("Something", "?");
        //FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(12345);

        FirstUI.addText("________________________", 20, 3, 1);
        FirstUI.addText("Edit:", 30, 3, 1);
        FirstUI.addButtonToTheGrid("Rocket", 1,1);
        FirstUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Rocket").Gettext() + " was clicked");
                RocketParameterMenu(primaryStage);
            }
        });
        FirstUI.addButtonToTheGrid("Environment", 1,1);
        FirstUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                EnvironmentParameterMenu(primaryStage);
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

    }
    private Rocket CalculateTrajectory(){

        Simulation sim = new Simulation();
        //double[] values = {0.01, 0.0, 0.0, 90.0, 0.0, 60.0, 0.0, 50, 21, 0.31, 0.2, 6000.0, 0.474};
        //double[] values={0.01, 0.0, 0.0, 0.0, 4.077, 4.0, 0.31, 0.2, 1419.0, 0.474, 90.0, 0.0, 90.0, 0.0};
        sim.run_simulation(inputs);//hardcode values for now! Change this to hashmap
        /*
        double time_step = values[0];
        double launch_latitude = values[1], launch_longitude = values[2];
        double launch_altitude = values[3], launch_azimuth = values[4];
        double wind_speed = values[5], wind_angle = values[6];
        double fuel_mass = values[7], dry_mass = values[8];
        double drag_coefficient = values[9], nose_diameter = values[10];
        double engine_thrust = values[11], burn_rate = values[12];
         */

        return sim.getRocket();
    }
    private void SimulationMenu(Rocket theRocket) {
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

        SecondUI.addText("________________________", 20, 3, 1);
        SecondUI.addText("Options:", 30, 3, 1);
        SecondUI.addButtonToTheGrid("Rocket", 1,1);
        SecondUI.NormalButtonHashMap.get("Rocket").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Rocket").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Rocket").Gettext() + " was clicked");
                //HashMap<String,Double> Rocketfields = RocketParameterMenu(primaryStage);
            }
        });
        SecondUI.addButtonToTheGrid("Environment", 1,1);
        SecondUI.NormalButtonHashMap.get("Environment").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                //HashMap<String,Double> Environmentfields = EnvironmentParameterMenu(primaryStage);
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
                ThirdUI.GetStage().close();
            }
        });

        setDefaultsRocket(ThirdUI);
        ThirdUI.Configure2D();
        ThirdUI.GetStage().showAndWait();
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
        FourthUI.addFieldToTheGrid("Altitude","*");
        FourthUI.addFieldToTheGrid("Azimuth","*");
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
                inputs.put("Playback Speed", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Playback Speed").getValue()));
                inputs.put("Simulation Duration", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Simulation Duration").getValue()));
                inputs.put("Wind Speed", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Speed").getValue()));
                inputs.put("Wind Angle", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Wind Angle").getValue()));
                inputs.put("Altitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Altitude").getValue()));
                inputs.put("Azimuth", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Azimuth").getValue()));
                inputs.put("Latitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Latitude").getValue()));
                inputs.put("Longitude", Double.parseDouble(FourthUI.NormalFieldHashMap.get("Longitude").getValue()));
                FourthUI.GetStage().close();
            }
        });

        setDefaultsEnvironment(FourthUI);
        FourthUI.Configure2D();
        FourthUI.GetStage().showAndWait();

    }
    public void setDefaultsRocket(NormalUserInterface ThirdUI){
        ThirdUI.NormalFieldHashMap.get("Fuel Mass").getThing2().setText("20");
        ThirdUI.NormalFieldHashMap.get("Hull Mass").getThing2().setText("0.5");
        ThirdUI.NormalFieldHashMap.get("Engine Mass").getThing2().setText("3");
        ThirdUI.NormalFieldHashMap.get("Payload Mass").getThing2().setText("1");
        ThirdUI.NormalFieldHashMap.get("Engine Thrust").getThing2().setText("3000");
        ThirdUI.NormalFieldHashMap.get("Burn Rate").getThing2().setText("0.474");
        ThirdUI.NormalFieldHashMap.get("Nose Diameter").getThing2().setText("0.2");
        ThirdUI.NormalFieldHashMap.get("Drag Coefficient").getThing2().setText("0.31");
    }
    public void setDefaultsEnvironment(NormalUserInterface FourthUI){
        FourthUI.NormalFieldHashMap.get("Time Step").getThing2().setText("0.01");
        FourthUI.NormalFieldHashMap.get("Playback Speed").getThing2().setText("5");
        FourthUI.NormalFieldHashMap.get("Simulation Duration").getThing2().setText("300");;
        FourthUI.NormalFieldHashMap.get("Wind Speed").getThing2().setText("0");
        FourthUI.NormalFieldHashMap.get("Wind Angle").getThing2().setText("0");
        FourthUI.NormalFieldHashMap.get("Altitude").getThing2().setText("90");
        FourthUI.NormalFieldHashMap.get("Azimuth").getThing2().setText("0");
        FourthUI.NormalFieldHashMap.get("Latitude").getThing2().setText("0");
        FourthUI.NormalFieldHashMap.get("Longitude").getThing2().setText("0");
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



