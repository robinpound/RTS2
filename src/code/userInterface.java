package code;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class userInterface {
    //make new constructor with same name for preset values if needed

    final int windowHeight = 650;
    final int windowWidth = 900;
    final int gridPaneWidth = 250;
    GridPane thegrid = new GridPane();

    userInterface(Stage secondaryStage){

        //Stage
        stageDimensions(secondaryStage);

        // 3D
        SubScene subScene = displaySimulation();

        // 2D
        displayGridPaneAttributes();

        //Pane
        BorderPane pane = new BorderPane();
        pane.setRight(subScene);
        pane.setLeft(thegrid);
        Scene scene = new Scene(pane);
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("3D-Rocket Trajectory Simulator");
        secondaryStage.show();
    }
    private void displayGridPaneAttributes() {
        //2D
        Text title = new Text("Real Time Data:");
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        thegrid.add(title, 0, 0,2,1);
        /*
        NormalTextBox textbox2 = new NormalTextBox("Altitude",0,1,thegrid,"m");
        NormalTextBox textbox3 = new NormalTextBox("fuel",0,2,thegrid,"%");
        NormalTextBox textbox4 = new NormalTextBox("Speed",0,3,thegrid,"m/s");
         */

        NormalData TimeElapsed = new NormalData("Time Elapsed", 0,1,thegrid,"s");
        NormalData Position = new NormalData("Position(X,Y,Z)", 0,2,thegrid,"m");
        NormalData Velocity = new NormalData("Velocity", 0,3,thegrid,"m/s");
        NormalData Fuel = new NormalData("Fuel", 0,4,thegrid,"kg");
        NormalData Acceleration = new NormalData("Acceleration(mag)", 0,5,thegrid,"m/s^2");
        NormalData Thrust = new NormalData("Thrust(mag)", 0,6,thegrid,"N");
        NormalData Drag = new NormalData("Drag(mag)", 0,7,thegrid,"N");
        NormalData Mass = new NormalData("Mass(Dry+Wet)", 0,8,thegrid,"kg");
        NormalData Wind = new NormalData("Wind(X,Y)", 0,9,thegrid,"m/s");
        NormalData Atmosphere = new NormalData("Atmosphere", 0,10,thegrid,"kg/m^3");
        NormalData Gravity = new NormalData("Gravity", 0,11,thegrid,"N");

        Fuel.setText(1.000);
        Thrust.setText(123123.1231);
        Position.setText(100,100,100);
        NormalButton exitbutton = new NormalButton("Exit",0,13,1,1,thegrid);
        exitbutton.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
    }
    private SubScene displaySimulation() {
        //3D
        PerspectiveCamera camera = new PerspectiveCamera(true);
        Group root = new Group(camera);
        SubScene subScene = new SubScene(root, 0,0, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLUE);
        subScene.setCamera(camera);
        subScene.setHeight(windowHeight);
        subScene.setWidth(windowWidth-gridPaneWidth);
        return subScene;
    }
    public Stage stageDimensions(Stage secondaryStage){
        secondaryStage.setMinHeight(windowHeight);
        secondaryStage.setMinWidth(windowWidth);
        secondaryStage.setMaxHeight(windowHeight);
        secondaryStage.setMaxWidth(windowWidth);
        return secondaryStage;
    }
}
