package code;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.util.Duration;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        mainMenu();


        /*
        //Inputs UI (JavaFx)
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        double[] values = inputmethod(secondaryStage);
        //Simulation
        Simulation sim = new Simulation();
        sim.set_state(values[0],values[1],values[2]); //double time_step, double wind_speed, double wind_angle
        List<List> arraylist2D = sim.run_simulation(values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
            // double fuel_mass,double dry_mass,double drag_coefficient,double nose_diameter,double engine_thrust,double burn_rate,double altitude,double azimuth
        // OUTPUT
            //display the rocket launch
        AnimationWindow ani = new AnimationWindow();
        ani.animationstart(primaryStage, arraylist2D);
            //graphs will be called by the animation window
         */


    }

    public void mainMenu(){

        //sets up Stage
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle("Main Menu");

        //sets up GridPane
        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.TOP_CENTER);
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);

        //sets up Button
        NormalButton new_btn = new NormalButton(0,0,"   New Simulation   ",menuGrid);
        NormalButton load_btn = new NormalButton(1,1,"   Load Simulation   ",menuGrid);
        NormalButton exit_btn = new NormalButton(2,2,"   Exit Simulation   ",menuGrid);
        NormalButton help_btn = new NormalButton(3,3,"?",menuGrid);

        new_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(new_btn.Gettext() + "was clicked");
            }
        });






        //sets up Scene
        Scene menuScene = new Scene(menuGrid);
        secondaryStage.setScene(menuScene);
        secondaryStage.showAndWait();


    }

    public static void main(String[] args) {
        launch(args);
    }
}



