package code;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;

public class NormalData {

    private final int column = 0;
    private int row;

    Label thing = new Label();
    Label thing2 = new Label();
    Label thing3 = new Label();
    DecimalFormat df2 = new DecimalFormat("0.00");


    NormalData(String name, int row, GridPane grid, String units){
        this.row = row;
        this.thing.setText(name + ": ");
        this.thing3.setText(" " + units);
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

        thing2.setMaxWidth(145);
        thing2.setMinWidth(145);

        grid.getColumnConstraints().removeAll();// tidy up any previous constraints that may have been set up
        grid.add(thing, column, row);
        grid.add(thing2,column+1, row);
        grid.add(thing3,column+2, row);
        ColumnConstraints defaultconstraints = new ColumnConstraints();// default constraints
        ColumnConstraints thing2constraints = new ColumnConstraints();// constraints to right justify data fields


        thing2constraints.setFillWidth(true);
        thing2constraints.setHalignment(HPos.RIGHT);

        for(int i=0; i<column+2; i++) {
            grid.getColumnConstraints().add(i, defaultconstraints);// add empty constraints for preceding columns
        }
        grid.getColumnConstraints().add(1, thing2constraints);// add special constraints for data column
    }
}
