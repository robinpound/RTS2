package code;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javax.swing.*;

public class NormalButton {
    private Button btn;
    private String text;
    private int column = 0;
    int row,columnSpan,rowSpan;
    GridPane grid;
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
    public void SetColumn(int number){
        column = number;
        grid.getChildren().remove(btn);
        grid.add(btn,column,row,columnSpan,rowSpan);

    }


}





