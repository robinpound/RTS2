package code;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NormalTextBox {

    String name;
    int column;
    int row;

    Label thing = new Label();
    TextField thing2 = new TextField();
    Label thing3 = new Label();

    NormalTextBox(String name, int column, int row, GridPane grid, String units){
        this.name = name;
        this.column = column;
        this.row = row;
        this.thing.setText(name+":");
        this.thing3.setText(units);

        addsobjects(grid);

    }
    private void addsobjects(GridPane grid){
        grid.add(thing, column, row);
        grid.add(thing2, column+1, row);
        grid.add(thing3,column+2, row);
    }
}

