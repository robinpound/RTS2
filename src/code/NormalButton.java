package code;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javax.swing.*;

public class NormalButton {
    private Button btn;
    private String text;
    private int column = 0;
    private int row,columnSpan,rowSpan;
    private GridPane grid;
    NormalButton(String text, int row, int columnSpan, int rowSpan, GridPane grid){
        this.text = text;
        this.grid = grid;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        btn = new Button(text);
        grid.add(btn,column,row,columnSpan,rowSpan);
    }
    public Button GetButton(){
        return btn;
    }
    public String Gettext(){
        return text;
    }
    public void SetColumn(int Bcolumn){
        column = Bcolumn;
        removeAndAdd();
    }
    public void setCoordinates(int Bcolumn, int Brow){
        column = Bcolumn;
        row = Brow;
        removeAndAdd();

    }
    private void removeAndAdd(){
        grid.getChildren().remove(btn);
        grid.add(btn,column,row,columnSpan,rowSpan);
    }
    public void translateObject(int YTranslate, int XTranslate){
        grid.getChildren().remove(btn);
        btn.setTranslateY(YTranslate);
        btn.setTranslateX(XTranslate);
        grid.add(btn,column,row,columnSpan,rowSpan);
    }
}





