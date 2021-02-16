package code;

import com.sun.rowset.internal.Row;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
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
    public void createTable(){

        TreeTableView<theRecord> treeTableView = new TreeTableView<>();

        TreeTableColumn<theRecord, String> useridcolumn = new TreeTableColumn<>("UserID");
        TreeTableColumn<theRecord, String> usercolumn = new TreeTableColumn<>("User");
        TreeTableColumn<theRecord, String> titlecolumn = new TreeTableColumn<>("Title");
        TreeTableColumn<theRecord, String> datecolumn = new TreeTableColumn<>("Date");
        TreeTableColumn<theRecord, String> viewstatuscolumn = new TreeTableColumn<>("Password Protection");

        useridcolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("userid"));
        usercolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("username"));
        titlecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        datecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        viewstatuscolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("passwordprotection"));

        //this is not working, something to do with the names. 

        treeTableView.getColumns().add(useridcolumn);
        treeTableView.getColumns().add(usercolumn);
        treeTableView.getColumns().add(titlecolumn);
        treeTableView.getColumns().add(datecolumn);
        treeTableView.getColumns().add(viewstatuscolumn);

        TreeItem rocket1 = new TreeItem(new theRecord(1, "Robin", "Rocket1", "15/02/2021", true));
        TreeItem robin = new TreeItem(new theRecord(null, "Robin"," "," ",null));
        TreeItem rocket2 = new TreeItem(new theRecord(1, "Sam", "Rocket2", "15/02/2021", false));
        TreeItem sam = new TreeItem(new theRecord(null, "Sam"," "," ",null));

        robin.getChildren().add(rocket1);
        sam.getChildren().add(rocket2);

        TreeItem records = new TreeItem(new theRecord(null, "RECORDS"," "," ",null));
        records.getChildren().add(robin);
        records.getChildren().add(sam);

        treeTableView.setRoot(records);
        theGrid.add(treeTableView,0,0);


        /*
        TreeTableView<theRecord> treeTableView = new TreeTableView<>();

        TreeTableColumn<theRecord, String> treeTableColumn1 = new TreeTableColumn<>("Brand");
        TreeTableColumn<theRecord, String> treeTableColumn2 = new TreeTableColumn<>("Model");

        treeTableColumn1.setCellValueFactory(new TreeItemPropertyValueFactory<>("brand"));
        treeTableColumn2.setCellValueFactory(new TreeItemPropertyValueFactory<>("model"));

        treeTableView.getColumns().add(treeTableColumn1);
        treeTableView.getColumns().add(treeTableColumn2);

        TreeItem mercedes1 = new TreeItem(new theRecord("Mercedes", "SL500"));
        TreeItem mercedes2 = new TreeItem(new theRecord("Mercedes", "SL500 AMG"));
        TreeItem mercedes3 = new TreeItem(new theRecord("Mercedes", "CLA 200"));

        TreeItem mercedes = new TreeItem(new theRecord("Mercedes", "..."));
        mercedes.getChildren().add(mercedes1);
        mercedes.getChildren().add(mercedes2);

        TreeItem audi1 = new TreeItem(new theRecord("Audi", "A1"));
        TreeItem audi2 = new TreeItem(new theRecord("Audi", "A5"));
        TreeItem audi3 = new TreeItem(new theRecord("Audi", "A7"));

        TreeItem audi = new TreeItem(new theRecord("Audi", "..."));
        audi.getChildren().add(audi1);
        audi.getChildren().add(audi2);
        audi.getChildren().add(audi3);

        TreeItem cars = new TreeItem(new theRecord("Cars", "..."));
        cars.getChildren().add(audi);
        cars.getChildren().add(mercedes);

        treeTableView.setRoot(cars);
        theGrid.add(treeTableView,0,0);

         */

    }

}
