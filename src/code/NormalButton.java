package code;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class NormalButton {
    private Button btn;
    private String text;

    NormalButton(int collumn, int row, String text, GridPane grid){
        this.text = text;
        btn = new Button(text);
        grid.add(btn,collumn,row);
    }
    public Button GetButton(){
        return btn;
    }
    public String Gettext(){
        return text;
    }
}





