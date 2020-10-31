package code;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class NormalButton {
    private Button btn;
    private String text;

    NormalButton(String text, int column, int row, int columnSpan, int rowSpan, GridPane grid){
        this.text = text;
        btn = new Button(text);
        grid.add(btn,column,row,columnSpan,rowSpan);
    }
    public Button GetButton(){
        return btn;
    }
    public String Gettext(){
        return text;
    }
}





