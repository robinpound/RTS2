package code;

import com.sun.rowset.internal.Row;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.HashMap;

public class NormalUserInterface {

    final int windowHeight;
    final int windowWidth;
    int gridPaneWidth;

    int rowCounter = 0;
    HashMap<String, NormalData> NormalDataHashMap = new HashMap<>();
    HashMap<String, NormalButton> NormalButtonHashMap = new HashMap<>();
    HashMap<String, NormalTextBox> NormalFieldHashMap = new HashMap<>();
    HashMap<String, Text> NormalTextHashMap = new HashMap<>();

    protected Stage theStage;
    protected GridPane theGrid;
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
        theGrid = new GridPane();
        theGrid.setHgap(Hgap);
        theGrid.setVgap(Vgap);
        Insets ins = new Insets(8.0,16.0,8.8,16.0);
        theGrid.setPadding(ins);
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
        root = new Group();
        subScene = new SubScene(root, 0,0, true, SceneAntialiasing.BALANCED);
    }
    public void setSimulation(){//if 3d
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        subScene.setHeight(windowHeight);
        subScene.setWidth(windowWidth-gridPaneWidth);
    }
    public void Configure(){ //if 3d
        BorderPane pane = new BorderPane();

        pane.setRight(subScene);
        pane.setLeft(theGrid);

        scene = new Scene(pane);
        theStage.setScene(scene);
    }
    public void setGap(int vgap, int hgap){
        theGrid.setVgap(vgap);
        theGrid.setHgap(hgap);
    }

//-----------------------------------------------------------------------------------------------
    public void Configure2D(){
        Scene scene = new Scene(theGrid);
        theStage.setScene(scene);
    }
    public void addNormalDataToTheGrid(String name, String units){
        NormalDataHashMap.put(name,new NormalData(name,rowCounter,theGrid,units));
        rowCounter ++;
    }

    public void addButtonToTheGrid(String name, int columnSpan, int rowSpan){
        NormalButton button = new NormalButton(name,rowCounter,columnSpan,rowSpan,theGrid);
        button.GetButton().setMinSize(110.0,30.0);
        NormalButtonHashMap.put(name, button);
        rowCounter ++;
    }
    public void addFieldToTheGrid(String name, String units){
        NormalFieldHashMap.put(name, new NormalTextBox(name, rowCounter, theGrid, units));
        rowCounter ++;
    }
    public void addText(String text, int size, int colspan, int rowspan){
        Text title = new Text(text);
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, size));
        theGrid.add(title, 0, rowCounter,colspan,rowspan);
        NormalTextHashMap.put(text,title);
        rowCounter++;
    }
    public void addCoordinateText(String text, int size, int Tcolumn, int Trow, int colspan, int rowspan){
        Text title = new Text(text);
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, size));
        theGrid.add(title, Tcolumn, Trow,colspan,rowspan);
        NormalTextHashMap.put(text,title);
        rowCounter++;
    }
    public Stage GetStage(){
        return theStage;
    }
    //get functions for hashmaps, to check for invalid data types

    //-----------------------------------------------------------------------------------------------
    //DATABASE STUFF

    public void createTable(){
        TreeTableView<> treeTableView = new TreeTableView<>();

    }
}
