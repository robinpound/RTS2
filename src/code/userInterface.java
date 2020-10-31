package code;

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
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class userInterface {
    //make new constructor with same name for preset values if needed
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
    userInterface(Stage primaryStage){
        // 3D
        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.GREENYELLOW));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (rotateX, rotateY, new Translate(0, 0, -20));

        Group root3D = new Group(camera,box);

        SubScene subScene = new SubScene(root3D, 1000, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.AQUAMARINE);
        subScene.setCamera(camera);

        // 2D
        BorderPane pane = new BorderPane();
        pane.setCenter(subScene);
        Button button = new Button("Reset");
        button.setOnAction(e->{
            rotateX.setAngle(-20);
            rotateY.setAngle(-20);
        });
        CheckBox checkBox = new CheckBox("Line");
        checkBox.setOnAction(e->{
            box.setDrawMode(checkBox.isSelected()? DrawMode.LINE:DrawMode.FILL);
        });
        ToolBar toolBar = new ToolBar(button, checkBox);
        toolBar.setOrientation(Orientation.VERTICAL);

        GridPane thegrid = new GridPane();
        NormalTextBox textbox = new NormalTextBox("fuel",1,1,thegrid,"Kg");

        pane.setRight(thegrid);
        pane.setPrefSize(300,300);

        Scene scene = new Scene(pane);

        scene.setOnMousePressed((MouseEvent me) -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("3D SubScene");
        primaryStage.show();
    }

}
