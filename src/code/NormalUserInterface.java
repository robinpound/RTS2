package code;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
    public void createRocketTable(Statement statement){

        TreeTableView<theRecord> treeTableView = new TreeTableView<>();

        TreeTableColumn<theRecord, String> useridcolumn = new TreeTableColumn<>("RecordID");
        TreeTableColumn<theRecord, String> usercolumn = new TreeTableColumn<>("Username");
        TreeTableColumn<theRecord, String> titlecolumn = new TreeTableColumn<>("Title");
        TreeTableColumn<theRecord, String> datecolumn = new TreeTableColumn<>("Creation Date");
        TreeTableColumn<theRecord, String> viewstatuscolumn = new TreeTableColumn<>("Password Protection");

        useridcolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("recordID"));
        usercolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("username"));
        titlecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        datecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("creationdate"));
        viewstatuscolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("passwordprotection"));
        //this is not working, something to do with the names.
        useridcolumn.setMinWidth(100);
        usercolumn.setMinWidth(100);
        titlecolumn.setMinWidth(100);
        datecolumn.setMinWidth(100);
        viewstatuscolumn.setMinWidth(100);
        treeTableView.getColumns().add(useridcolumn);
        treeTableView.getColumns().add(usercolumn);
        treeTableView.getColumns().add(titlecolumn);
        treeTableView.getColumns().add(datecolumn);
        treeTableView.getColumns().add(viewstatuscolumn);

        ArrayList<HashMap<String, String>> rocketTable = getRocketTable(statement);

        HashMap<String, TreeItem> usertree = new HashMap<>();

        for(int i=0; i<rocketTable.size(); i++) {
            String username = rocketTable.get(i).get("username");
            if(!usertree.containsKey(username)) {
                usertree.put(username, new TreeItem(new theRecord(null, username," "," ",null)));
            }
            String recordID = rocketTable.get(i).get("recordID");
            String title = rocketTable.get(i).get("title");
            String creationdate = rocketTable.get(i).get("creationdate");
            String password = rocketTable.get(i).get("password");
            usertree.get(username).getChildren().add(new TreeItem(new theRecord(recordID, username, title, creationdate, password)));
        }

        //-------------------------------------------------------------------------------
        /*
        TreeItem robin = new TreeItem(new theRecord(null, "Robin"," "," ",null));
        TreeItem rocket1 = new TreeItem(new theRecord("1", "Robin", "Rocket1", "15/02/2021", "true"));
        robin.getChildren().add(rocket1);

        TreeItem sam = new TreeItem(new theRecord(null, "Sam"," "," ",null));
        TreeItem rocket2 = new TreeItem(new theRecord("2", "Sam", "Rocket2", "15/02/2021", "false"));
        sam.getChildren().add(rocket2);

         */

        TreeItem<theRecord> records = new TreeItem<>(new theRecord(null, "RECORDS"," "," ",null));

        for(TreeItem useritem : usertree.values()){
            records.getChildren().add(useritem);
        }

        /*
        records.getChildren().add(robin);
        records.getChildren().add(sam);

         */



        treeTableView.setRoot(records);
        theGrid.add(treeTableView,0,0);



    }
    private ArrayList getRocketTable(Statement statement){
        ArrayList<HashMap<String, String>> rocketTable = new ArrayList<>();
        try{
            ResultSet result = statement.executeQuery("SELECT * FROM rockets");
            while(result.next()){
                HashMap<String,String> record = new HashMap<>();
                record.put("recordID", result.getString("recordID"));
                record.put("username", result.getString("username"));
                record.put("title", result.getString("title"));
                record.put("creationdate", result.getString("creationdate"));
                record.put("password", result.getString("password"));
                record.put("fuelmass", result.getString("fuelmass"));
                record.put("hullmass", result.getString("hullmass"));
                record.put("enginemass", result.getString("enginemass"));
                record.put("payloadmass", result.getString("payloadmass"));
                record.put("enginethrust", result.getString("enginethrust"));
                record.put("burnrate", result.getString("burnrate"));
                record.put("nosediameter", result.getString("nosediameter"));
                record.put("dragcoefficient", result.getString("dragcoefficient"));
                rocketTable.add(record);
            }
        }catch (Exception e){System.out.println(e.getMessage());}

        for (int i = 0; i < rocketTable.size(); i++){
            System.out.println(rocketTable.get(i));

        }


        return rocketTable;
    }

}
