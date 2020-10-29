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

    NormalTextBox fuel_mass = null;
    NormalTextBox dry_mass = null;
    NormalTextBox drag_coefficient = null;
    NormalTextBox nose_diameter = null;
    NormalTextBox engine_thrust = null;
    NormalTextBox burn_rate = null;
    NormalTextBox altitude = null;
    NormalTextBox azimuth = null;
    NormalTextBox time_step = null;
    NormalTextBox wind_speed = null;
    NormalTextBox wind_angle = null;

    boolean is_clicked = false;


    @Override
    public void start(Stage primaryStage) throws Exception{

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
    }

    public static void main(String[] args) {
        launch(args);
    }

    public double[] inputmethod(Stage secondaryStage){
        GridPane thegrid = new GridPane();
        double[] values = new double[11];
        //boolean launchclicked = false;

        //---Do JavaFx inputs here for set state---  INPUT
        secondaryStage.setTitle("3D Rocket Trajectory Simulator ");

        thegrid.setAlignment(Pos.TOP_LEFT);
        thegrid.setHgap(10);
        thegrid.setVgap(10);
        thegrid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("3D Rocket Trajectory Simulator");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        thegrid.add(scenetitle, 0, 0, 4, 1);

        Text subtitle = new Text("");
        subtitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        thegrid.add(subtitle, 1, 1, 2, 1);

        fuel_mass = new NormalTextBox("fuel_mass", 0,2,thegrid,"(kg)");
        dry_mass = new NormalTextBox("dry_mass", 0,3,thegrid,"(kg)");
        drag_coefficient = new NormalTextBox("drag_coefficient", 0,4,thegrid,"(None)");
        nose_diameter = new NormalTextBox("nose_diameter", 0,5,thegrid,"(m)");
        engine_thrust = new NormalTextBox("engine_thrust", 0,6,thegrid,"(N)");
        burn_rate = new NormalTextBox("burn_rate", 0,7,thegrid,"(kg/s)");
        altitude = new NormalTextBox("altitude", 0,8,thegrid,"(vertical degrees)");
        azimuth = new NormalTextBox("azimuth", 0,9,thegrid,"(horizontal degrees)");
        time_step = new NormalTextBox("time_step", 0,10,thegrid,"(seconds/iteration)");
        wind_speed = new NormalTextBox("wind_speed", 0,11,thegrid,"(m/s)");
        wind_angle = new NormalTextBox("wind_angle", 0,12,thegrid,"(degrees)");
        setDefaults();

        Button btn = new Button("                 Launch                 ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        thegrid.add(hbBtn, 1, 13);

        Button btn2 = new Button("Default Values");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        thegrid.add(hbBtn2, 2, 13);

        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                secondaryStage.close();
                is_clicked = true;
            }
        });

        btn2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setDefaults();
            }
        });




        Scene thescene = new Scene(thegrid);
        secondaryStage.setScene(thescene);
        secondaryStage.showAndWait();

        //checking if clicked is not true, then close
        if (!is_clicked){
            System.exit(0);
        }

        values[0] = Double.parseDouble(time_step.thing2.getCharacters().toString());
        values[1] = Double.parseDouble(wind_speed.thing2.getCharacters().toString());
        values[2] = Double.parseDouble(wind_angle.thing2.getCharacters().toString());
        values[3] = Double.parseDouble(fuel_mass.thing2.getCharacters().toString());
        values[4] = Double.parseDouble(dry_mass.thing2.getCharacters().toString());
        values[5] = Double.parseDouble(drag_coefficient.thing2.getCharacters().toString());
        values[6] = Double.parseDouble(nose_diameter.thing2.getCharacters().toString());
        values[7] = Double.parseDouble(engine_thrust.thing2.getCharacters().toString());
        values[8] = Double.parseDouble(burn_rate.thing2.getCharacters().toString());
        values[9] = Double.parseDouble(altitude.thing2.getCharacters().toString());
        values[10] = Double.parseDouble(azimuth.thing2.getCharacters().toString());

        return values;

    }

    private void setDefaults(){
        time_step.thing2.setText("0.01");
        wind_speed.thing2.setText("0");
        wind_angle.thing2.setText("180");
        fuel_mass.thing2.setText("4.077");
        dry_mass.thing2.setText("4"); // engine dry_mass is 2.839
        drag_coefficient.thing2.setText("0.31");
        nose_diameter.thing2.setText("0.2");
        engine_thrust.thing2.setText("1419");
        burn_rate.thing2.setText("0.474");
        altitude.thing2.setText("90"); //90
        azimuth.thing2.setText("0"); //0
    }
}



