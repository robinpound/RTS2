package code;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class Main extends Application {

    Boolean CloseClicked = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage secondaryStage = new Stage();
        LoginMenu(secondaryStage);
        BuildingMenu(secondaryStage);
        SimulationMenu(secondaryStage);
    }
    private void LoginMenu(Stage secondaryStage){

        //set up Stage
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle("Main Menu");

        //set up GridPane
        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.TOP_CENTER);
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);

        //set up Text
        Text title = new Text("3D-Rocket Trajectory Simulator(all wrong and unfinished):");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        menuGrid.add(title,1,0,1,1);

        //set up Buttons

        NormalButton new_btn = new NormalButton("   thing   ",1,1,1,menuGrid);
        NormalButton load_btn = new NormalButton("   thing2   ",2,1,1,menuGrid);
        NormalButton exit_btn = new NormalButton("   Exit   ",3,1,1,menuGrid);
        NormalButton help_btn = new NormalButton("?",7,1,1,menuGrid);
        new_btn.SetColumn(1);
        load_btn.SetColumn(1);
        exit_btn.SetColumn(1);
        help_btn.SetColumn(7);

        //adding Buttons
        new_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(new_btn.Gettext() + "was clicked");
                secondaryStage.close();
            }
        });
        load_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(load_btn.Gettext() + "was clicked");
                //CALL LOADING FUNCITON
                secondaryStage.close();
            }
        });
        exit_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(exit_btn.Gettext() + "was clicked");
                System.exit(0);
            }
        });
        help_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(help_btn.Gettext() + "was clicked");
                //CALL HELP FUNCITON
                secondaryStage.close();
            }
        });

        //sets up Scene
        Scene menuScene = new Scene(menuGrid);
        secondaryStage.setScene(menuScene);
        secondaryStage.showAndWait();


    }
    private void BuildingMenu(Stage secondaryStage){

        //set up Stage
        //thirdStage.initModality(Modality.APPLICATION_MODAL);


        NormalUserInterface FirstUI = new NormalUserInterface(650, 950, secondaryStage);
        secondaryStage.setTitle("Building Menu");
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
                //Rocket settings here
            }
        });
        FirstUI.addButtonToTheGrid("Environment", 1,1);
        FirstUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(FirstUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                //Environment Settings here
            }
        });
        FirstUI.addButtonToTheGrid("Launch", 1,1);
        FirstUI.NormalButtonHashMap.get("Launch").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                secondaryStage.close();
                System.out.println(FirstUI.NormalButtonHashMap.get("Launch").Gettext() + " was clicked");
            }
        });
        FirstUI.addButtonToTheGrid("Exit", 1,1);
        FirstUI.NormalButtonHashMap.get("Exit").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                secondaryStage.close();
                System.out.println(FirstUI.NormalButtonHashMap.get("Exit").Gettext() + " was clicked");
                System.exit(0);
            }
        });


        FirstUI.GetStage().showAndWait();

    }
    private void SimulationMenu(Stage secondaryStage) {
        NormalUserInterface SecondUI = new NormalUserInterface(700, 1000, secondaryStage);

        SecondUI.createGridPane(250,0,2);
        SecondUI.addStageDimensions();
        SecondUI.addCameraAndSubscene();
        SecondUI.setSimulation();
        SecondUI.Configure();

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
                //Rocket settings here
            }
        });
        SecondUI.addButtonToTheGrid("Environment", 1,1);
        SecondUI.NormalButtonHashMap.get("Environment").SetColumn(0);
        SecondUI.NormalButtonHashMap.get("Environment").GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(SecondUI.NormalButtonHashMap.get("Environment").Gettext() + " was clicked");
                //Environment Settings here
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
                secondaryStage.close();
                System.out.println(SecondUI.NormalButtonHashMap.get("Exit").Gettext() + " was clicked");
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}



