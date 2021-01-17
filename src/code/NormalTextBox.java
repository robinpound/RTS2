package code;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    private GridPane grid;

    private Label thing = new Label();
    private TextField thing2 = new TextField();
    private Label thing3 = new Label();
    private PasswordField thing4 = new PasswordField();

    NormalTextBox(String name, int row, GridPane grid, String units){
        this.name = name;
        this.row = row;
        this.thing.setText(name+":");
        this.thing3.setText(units);
        this.grid = grid;
        addsobjects(grid);

    }
    public void setCoordinates(int column, int Frow){
        this.column = column;
        this.row = Frow;
        grid.getChildren().remove(thing);
        grid.getChildren().remove(thing2);
        grid.getChildren().remove(thing3);
        grid.add(thing, column, row);
        grid.add(thing2,column+1, row);
        grid.add(thing3,column+2, row);
    }
    private void addsobjects(GridPane grid){
        grid.add(thing, column, row);
        grid.add(thing2,column+1, row);
        grid.add(thing3,column+2, row);
    }
    public String getValue(){
        return thing2.getCharacters().toString();
    }
    public TextField getThing2(){
        return thing2;
    }
    public TextField getThing4(){
        return thing4;
    }
    public void setCoordinatespassword(int column, int Frow){
        this.column = column;
        this.row = Frow;
        grid.getChildren().remove(thing);
        grid.getChildren().remove(thing2);
        grid.getChildren().remove(thing3);
        grid.add(thing, column, row);
        grid.add(thing4,column+1, row);
        grid.add(thing3,column+2, row);
    }
}

