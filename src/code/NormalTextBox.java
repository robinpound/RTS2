package code;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import sun.swing.MenuItemLayoutHelper;

public class NormalTextBox {

    private String name;
    private int column = 0;
    private int row;

    private Label thing = new Label();
    private TextField thing2 = new TextField();
    private Label thing3 = new Label();

    NormalTextBox(String name, int row, GridPane grid, String units){
        this.name = name;
        this.row = row;
        this.thing.setText(name+":");
        this.thing3.setText(units);

        addsobjects(grid);

    }
    public void setColumn(int column){
        this.column = column;
    }
    private void addsobjects(GridPane grid){
        grid.add(thing, column, row);
        grid.add(thing2,column+1, row);
        grid.add(thing3,column+2, row);
    }
}

