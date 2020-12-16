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
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage secondaryStage = new Stage();
        mainMenu(secondaryStage);
        //userInterface ui = new userInterface(secondaryStage);

        NormalUserInterface ui = new NormalUserInterface(650, 950,secondaryStage);

        ui.createGridPane(250);
        ui.addStageDimensions();
        ui.addCameraAndSubscene();
        ui.setSimulation();
        ui.FunctionNameHere();
        ui.addNormalDataToTheGrid("bob", "semi-retired");
        ui.addNormalDataToTheGrid("robin", "bubbles");
        ui.NormalDataHashMap.get("robin").setText(4.2343242432432,7.374637248632874,2.2310957325692318748321964);
        ui.addNormalDataToTheGrid("steve", "bald");
        ui.NormalDataHashMap.get("steve").setText(4.2,7.3,2.2);
        ui.addNormalDataToTheGrid("mum", "old");
        ui.addNormalDataToTheGrid("alan", "semi-retired");
        ui.addButtonToTheGrid("exit", 2,2);
        ui.NormalButtonHashMap.get("exit").SetColumn(5);



    }
    Boolean CloseClicked = false;

    private void mainMenu(Stage secondaryStage){

        //set up Stage
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

        NormalButton new_btn = new NormalButton("   New Simulation   ",1,1,1,menuGrid);
        NormalButton load_btn = new NormalButton("   Load Simulation   ",2,1,1,menuGrid);
        NormalButton exit_btn = new NormalButton("   Exit Simulation   ",3,1,1,menuGrid);
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
    private void mainMenu2(Stage secondaryStage){
        VBox vBox = new VBox(new Label("A JavaFX Label"));
        Scene scene = new Scene(vBox);

        Stage stage = new Stage();
        stage.setScene(scene);
    }
    public static void main(String[] args) {
        launch(args);
    }
}



