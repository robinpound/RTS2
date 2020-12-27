package code;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

public class createSphere {
    private double r,x,y,z;
    private Sphere sphere = new Sphere();
    private PhongMaterial phong = new PhongMaterial();

    createSphere(double r, double x, double y, double z){
        this.r = r;
        this.x = x;
        this.y = y;
        this.z = z;

        sphere.setRadius(this.r);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        sphere.setDrawMode(DrawMode.FILL);


    }
    public Sphere getobject(){
        return sphere;
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
        phong.setDiffuseMap(new Image(getClass().getResourceAsStream( "../pictures/4k Earth.jpg")));
    }
    public void setillumination(){
        phong.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("../pictures/4k Earth.jpg")));
    }
    public void spheresetmaterial(){
        sphere.setMaterial(phong);
    }
    public void insideSphere(){
        phong.setDiffuseMap(new Image(getClass().getResourceAsStream( "../pictures/4k Earth.jpg")));
        //continue tomorrow :)
    }
}
