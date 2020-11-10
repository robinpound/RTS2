package code;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;

public class NormalData {

    String name;
    int column;
    int row;

    Label thing = new Label();
    Text thing2 = new Text();
    Label thing3 = new Label();
    DecimalFormat df2 = new DecimalFormat("#.##");

    NormalData(String name, int column, int row, GridPane grid, String units){
        this.name = name;
        this.column = column;
        this.row = row;
        this.thing.setText(name+":");
        this.thing3.setText(" - " + units);
        addsobjects(grid);
    }
    public void setText(double number){
        thing2.setText(df2.format(number));
    }
    public void setText(double one,double two,double three){
        thing2.setText(df2.format(one) + " | " + df2.format(two) + " | " + df2.format(three));
    }
    public void setText(double one,double two){
        thing2.setText(df2.format(one) + " | " + df2.format(two));
    }
    private void addsobjects(GridPane grid){
        grid.add(thing, column, row);
        thing2.setTextAlignment(TextAlignment.RIGHT); //doesn't work!!!
        grid.add(thing2, column+1, row);
        grid.add(thing3,column+2, row);
        //grid.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);  //trying to set alignment of box within column - crashes
    }
}
