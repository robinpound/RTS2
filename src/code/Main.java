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

    Boolean CloseClicked = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage secondaryStage = new Stage();
        loginMenu(secondaryStage);
        BuildingMenu(secondaryStage);
        SimulationMenu(secondaryStage);

    }


    private void loginMenu(Stage secondaryStage){

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
    public void BuildingMenu(Stage secondaryStage){
        NormalUserInterface FirstUI = new NormalUserInterface(650, 950,secondaryStage);

        FirstUI.createGridPane(250);
        FirstUI.addStageDimensions();
        FirstUI.addCameraAndSubscene();
        FirstUI.setSimulation();
        FirstUI.Configure();

        FirstUI.addText("Launching Statistics:", 20, 3, 1);

        FirstUI.addNormalDataToTheGrid("Total Rocket Mass", "kg");
        FirstUI.addNormalDataToTheGrid("Delta-V", "m/s");
        FirstUI.addNormalDataToTheGrid("burn_rate", "kg/s");
        FirstUI.addNormalDataToTheGrid("drag-coefficient", "none");
        FirstUI.addNormalDataToTheGrid("something", "?");
        //FirstUI.NormalDataHashMap.get("Total Rocket Mass").setText(12345);

        FirstUI.addText("________________", 1, 3, 1);
        FirstUI.addText("Edit:", 20, 3, 1);

        FirstUI.addButtonToTheGrid("Rocket", 0,0);
        FirstUI.NormalButtonHashMap.get("Rocket").SetColumn(1);
        FirstUI.addButtonToTheGrid("Environment", 0,0);
        FirstUI.NormalButtonHashMap.get("Environment").SetColumn(1);
        FirstUI.addButtonToTheGrid("exit", 0,0);
        FirstUI.NormalButtonHashMap.get("exit").SetColumn(1);





    }
    private void SimulationMenu(Stage secondaryStage) {
    }

    public static void main(String[] args) {
        launch(args);
    }
}



