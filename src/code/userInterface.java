package code;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class userInterface {
    //make new constructor with same name for preset values if needed

    userInterface(Stage secondaryStage){
        //secondaryStage.setFullScreen(true);
        // 3D
        PerspectiveCamera camera = new PerspectiveCamera(true);
        Group root = new Group(camera);
        SubScene subScene = new SubScene(root, 1050, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.PINK);
        subScene.setCamera(camera);

        // 2D
        GridPane thegrid = new GridPane();
        Text title = new Text("Real Time Data:");
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        thegrid.add(title, 1, 1,2,1);
        NormalTextBox textbox2 = new NormalTextBox("Altitude",1,2,thegrid,"m");
        NormalTextBox textbox3 = new NormalTextBox("fuel",1,3,thegrid,"%");
        NormalTextBox textbox4 = new NormalTextBox("Speed",1,4,thegrid,"m/s");
        NormalButton exitbutton = new NormalButton("Exit",1,5,1,1,thegrid);
        exitbutton.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                System.exit(0);
            }
        });

        //pane
        BorderPane pane = new BorderPane();
        pane.setCenter(subScene);
        pane.setLeft(thegrid);
        pane.setPrefSize(0,0);

        Scene scene = new Scene(pane);

        secondaryStage.setScene(scene);
        secondaryStage.setTitle("3D-Rocket Trajectory Simulator");
        secondaryStage.show();
    }
}
