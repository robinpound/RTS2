package code;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Dictionary;
import java.util.HashMap;

public class NormalUserInterface {

    final int windowHeight;
    final int windowWidth;
    int gridPaneWidth;

    int rowCounter = 0;
    HashMap<String, NormalData> NormalDataHashMap = new HashMap<String, NormalData>();
    HashMap<String, NormalButton> NormalButtonHashMap = new HashMap<String, NormalButton>();
    HashMap<String, NormalTextBox> NormalFieldHashMap = new HashMap<String, NormalTextBox>();

    protected Stage theStage;
    protected GridPane thegrid;
    protected PerspectiveCamera camera;
    protected Group root;
    protected SubScene subScene;
    protected Scene scene;

//-----------------------------------------------------------------------------------------------
    NormalUserInterface(int windowHeight, int windowWidth, Stage theStage){ //constructor
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.theStage = new Stage();
        this.theStage.initOwner(theStage);
        this.theStage.initModality(Modality.APPLICATION_MODAL);
    }
    public void createGridPane(int gridPaneWidth, int Hgap, int Vgap){
        thegrid = new GridPane();
        thegrid.setHgap(Hgap);
        thegrid.setVgap(Vgap);
        Insets ins = new Insets(8.0,16.0,8.8,16.0);
        thegrid.setPadding(ins);
        this.gridPaneWidth = gridPaneWidth;
    }
    public void addStageDimensions(){
        theStage.setMinHeight(windowHeight);
        theStage.setMinWidth(windowWidth);
        theStage.setMaxHeight(windowHeight);
        theStage.setMaxWidth(windowWidth);

    }
    public void addCameraAndSubscene() {//if 3d
        //3D
        camera = new PerspectiveCamera(true);
        subScene = new SubScene(root, 0,0, true, SceneAntialiasing.BALANCED);
    }
    public void setSimulation(){//if 3d
        subScene.setFill(Color.BLUE);
        subScene.setCamera(camera);
        subScene.setHeight(windowHeight);
        subScene.setWidth(windowWidth-gridPaneWidth);
    }
    public void Configure(){ //if 3d
        BorderPane pane = new BorderPane();
        pane.setRight(subScene);
        pane.setLeft(thegrid);
        scene = new Scene(pane);
        theStage.setScene(scene);
    }






















//-----------------------------------------------------------------------------------------------
    public void Configure2D(){
        Scene scene = new Scene(thegrid);
        theStage.setScene(scene);
    }
    public void addNormalDataToTheGrid(String name, String units){
        NormalDataHashMap.put(name,new NormalData(name,rowCounter,thegrid,units));
        rowCounter ++;
    };

    public void addButtonToTheGrid(String name, int columnSpan, int rowSpan){
        NormalButton button = new NormalButton(name,rowCounter,columnSpan,rowSpan,thegrid);
        button.GetButton().setMinSize(110.0,30.0);
        NormalButtonHashMap.put(name, button);
        rowCounter ++;
    };
    public void addFieldToTheGrid(String name, String units){
        NormalFieldHashMap.put(name, new NormalTextBox(name, rowCounter, thegrid, units));
        rowCounter ++;
    };
    public void addText(String text, int size, int colspan, int rowspan){
        Text title = new Text(text);
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        thegrid.add(title, 0, rowCounter,3,1);
        rowCounter++;
    }
    public Stage GetStage(){
        return theStage;
    }
    //-----------------------------------------------------------------------------------------------
}
