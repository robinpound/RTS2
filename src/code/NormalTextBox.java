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

    NormalTextBox(String name, int column, int row, GridPane thegrid, String units){
        this.name = name;
        this.column = column;
        this.row = row;
        this.thing.setText(name+":");
        this.thing3.setText(units);

        addsobjects(thegrid);

    }
    private void addsobjects(GridPane thegrid){
        thegrid.add(thing, column, row);
        thegrid.add(thing2, column+1, row);
        thegrid.add(thing3,column+2, row);
    }
}

