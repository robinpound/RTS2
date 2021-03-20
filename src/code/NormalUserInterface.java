package code;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

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

    protected TreeTableView<theRecord> treeTableView = new TreeTableView<>();
    protected ArrayList<HashMap<String, String>> rocketTable = null;

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
    public void addgraph(int column){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("e.g. time");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("e.g. velocity");
        ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("2014");

        dataSeries1.getData().add(new XYChart.Data( 1, 567));
        dataSeries1.getData().add(new XYChart.Data( 5, 612));
        dataSeries1.getData().add(new XYChart.Data(10, 800));
        dataSeries1.getData().add(new XYChart.Data(20, 780));
        dataSeries1.getData().add(new XYChart.Data(40, 810));
        dataSeries1.getData().add(new XYChart.Data(80, 850));

        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("2015");
        dataSeries2.getData().add(new XYChart.Data( 2, 557));
        dataSeries2.getData().add(new XYChart.Data( 6, 652));
        dataSeries2.getData().add(new XYChart.Data(11, 850));
        dataSeries2.getData().add(new XYChart.Data(21, 750));
        dataSeries2.getData().add(new XYChart.Data(41, 850));
        dataSeries2.getData().add(new XYChart.Data(81, 850));

        scatterChart.getData().add(dataSeries1);
        scatterChart.getData().add(dataSeries2);
        theGrid.add(scatterChart, column, rowCounter);
        rowCounter++;
    }
    public Stage GetStage(){
        return theStage;
    }
    //get functions for hashmaps, to check for invalid data types

    //-----------------------------------------------------------------------------------------------
    public void createRocketTable(Statement statement){



        //TreeTableColumn<theRecord, String> useridcolumn = new TreeTableColumn<>("RecordID");
        TreeTableColumn<theRecord, String> usercolumn = new TreeTableColumn<>("Username");
        TreeTableColumn<theRecord, String> titlecolumn = new TreeTableColumn<>("Title");
        TreeTableColumn<theRecord, String> datecolumn = new TreeTableColumn<>("Creation Date");
        TreeTableColumn<theRecord, String> viewstatuscolumn = new TreeTableColumn<>("Protected");

        //useridcolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("recordID"));
        usercolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("username"));
        titlecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        datecolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("creationdate"));
        viewstatuscolumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("passwordprotection"));
        //useridcolumn.setMinWidth(100);
        usercolumn.setMinWidth(150);
        titlecolumn.setMinWidth(300);
        datecolumn.setMinWidth(100);
        viewstatuscolumn.setMinWidth(100);
        //treeTableView.getColumns().add(useridcolumn);
        treeTableView.getColumns().add(usercolumn);
        treeTableView.getColumns().add(titlecolumn);
        treeTableView.getColumns().add(datecolumn);
        treeTableView.getColumns().add(viewstatuscolumn);

        rocketTable = getRocketTableValues(statement);

        HashMap<String, TreeItem> usertree = new HashMap<>();

        for(int i=0; i<rocketTable.size(); i++) {
            String username = rocketTable.get(i).get("username");
            if(!usertree.containsKey(username)) {
                usertree.put(username, new TreeItem(new theRecord( " ", username," "," ",null)));
            }
            String recordID = rocketTable.get(i).get("recordID");
            String title = rocketTable.get(i).get("title");
            String creationdate = rocketTable.get(i).get("creationdate");
            String password = rocketTable.get(i).get("password");
            usertree.get(username).getChildren().add(new TreeItem(new theRecord(recordID," ",  title, creationdate, password)));
        }

        TreeItem<theRecord> records = new TreeItem<>(new theRecord(" ","Users"," "," ",null));

        for(TreeItem useritem : usertree.values()){
            records.getChildren().add(useritem);
        }


        treeTableView.setRoot(records);
        theGrid.add(treeTableView,0,rowCounter);
        rowCounter ++;



    }
    private ArrayList getRocketTableValues(Statement statement){
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
    public HashMap<String, Double> getSelectedRocketHashmap(){
        HashMap<String, Double> returningrocketinputs = new HashMap<>();
        int index = treeTableView.getSelectionModel().getFocusedIndex();
        String SelectedrecordID = treeTableView.getSelectionModel().getModelItem(index).getValue().getRecordID();
        //get hashmap
        for (int i = 0; i < rocketTable.size(); i++){
            if(rocketTable.get(i).get("recordID") == SelectedrecordID){
                returningrocketinputs.put("fuelmass",Double.parseDouble(rocketTable.get(i).get("fuelmass")));
                returningrocketinputs.put("hullmass",Double.parseDouble(rocketTable.get(i).get("hullmass")));
                returningrocketinputs.put("enginemass",Double.parseDouble(rocketTable.get(i).get("enginemass")));
                returningrocketinputs.put("payloadmass",Double.parseDouble(rocketTable.get(i).get("payloadmass")));
                returningrocketinputs.put("enginethrust",Double.parseDouble(rocketTable.get(i).get("enginethrust")));
                returningrocketinputs.put("burnrate",Double.parseDouble(rocketTable.get(i).get("burnrate")));
                returningrocketinputs.put("nosediameter",Double.parseDouble(rocketTable.get(i).get("nosediameter")));
                returningrocketinputs.put("dragcoefficient",Double.parseDouble(rocketTable.get(i).get("dragcoefficient")));
            }
        }

        return returningrocketinputs;
    }

}
