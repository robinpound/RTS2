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
        userInterface ui = new userInterface(primaryStage);

    }
    public void mainMenu(){

        //set up Stage
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle("Main Menu");

        //set up GridPane
        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.TOP_CENTER);
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);

        //set up Text
        Text title = new Text("3D-Rocket Trajectory Simulator:");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        menuGrid.add(title,1,0,1,1);

        //set up Buttons
        NormalButton new_btn = new NormalButton(1,1,1,1,"   New Simulation   ",menuGrid);
        NormalButton load_btn = new NormalButton(1,2,1,1,"   Load Simulation   ",menuGrid);
        NormalButton exit_btn = new NormalButton(1,3,1,1,"   Exit Simulation   ",menuGrid);
        NormalButton help_btn = new NormalButton(7,7,1,1,"?",menuGrid);

        //adding Buttons
        new_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(new_btn.Gettext() + "was clicked");
            }
        });
        load_btn.GetButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(load_btn.Gettext() + "was clicked");
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



