package code;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

public class createBox {
    private int d,w,h,x,y,z;
    private Box box = new Box();
    private PhongMaterial phong = new PhongMaterial();
    createBox(int d, int w, int h, int x, int y, int z){
        this.d = d;
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
        this.z = z;

        box.setDepth(d);
        box.setHeight(h);
        box.setWidth(w);
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);
        box.setDrawMode(DrawMode.FILL);

    }
    public Box getobject(){
            return box;
    }
    public void setcolourgreen(){
        phong.setDiffuseColor(Color.LIGHTGREEN);
    }
    public void setcolourred(){
        phong.setDiffuseColor(Color.RED);
    }
    public void setcolourblack(){
        phong.setDiffuseColor(Color.BLACK);
    }
    public void setcolourgrey(){
        phong.setDiffuseColor(Color.GREY);
    }
    public void settexture(){
        phong.setDiffuseMap(new Image(getClass().getResourceAsStream("multigrass.png")));
    }
    public void setillumination(){
        //phong.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("white.png")));
    }
    public void boxsetmaterial(){
        box.setMaterial(phong);
    }
}