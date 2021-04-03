package code;


import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;

public class TheAnimationWindow2 extends NormalUserInterface{
    private double ROCKETRADIUS;
    private double ROCKETHEIGHT;

    private final PhongMaterial rocketMat = new PhongMaterial();
    private Cylinder rocket = new Cylinder(ROCKETRADIUS,ROCKETHEIGHT);

    private double mouse_x = 0;
    private double mouse_y = 0;
    private double horizontal_cam = 0;
    private double vertical_cam = 0;

    TheAnimationWindow2(int windowHeight, int windowWidth, Stage theStage) {
        super(windowHeight, windowWidth, theStage);
    }
    public void SetRocketPosition(int x, int y, int z){

        rocket.translateXProperty().set(x);
        rocket.translateYProperty().set(y);
        rocket.translateZProperty().set(z);
        rocketMat.setDiffuseMap(new Image(getClass().getResourceAsStream( "../pictures/rocketmaterial.jfif")));
        rocket.setMaterial(rocketMat);
    }
    public void addRocket(){
        root.getChildren().add(rocket);
    }
    public void SetRocketOrientation(double altitude, double azimuth){
        System.out.println(altitude);
        System.out.println(azimuth);
        Vector3 v3 = new Vector3(altitude,azimuth);

        Point3D target_orientation = new Point3D(v3.getYVec(),v3.getXVec(),v3.getZVec()).normalize();          //converting to Point3D
        Point3D original_orientation = new Point3D(0,-1,0).normalize();                                //original orientation of rocket
        double rotation_angle = Math.toDegrees(Math.acos(original_orientation.dotProduct(target_orientation))); // finds angle between the two 3D vectors
        Point3D rotation_axis = original_orientation.crossProduct(target_orientation).normalize();              // finds axis that is orthogonal (right angles) to both origonal and target orientation vectors
        rocket.setRotationAxis(rotation_axis);
        rocket.setRotate(rotation_angle);

    }
    public void SetRocketSize(double radius){
        ROCKETRADIUS = radius;
        ROCKETHEIGHT = radius*10;
        rocket.setRadius(ROCKETRADIUS);
        rocket.setHeight(ROCKETHEIGHT);
        SetRocketPosition(20,(int)((-ROCKETHEIGHT/2)-1),600);
    }

    public void SetCameraPositiion(int x, int y, int z){
        camera.translateXProperty().set(x);                  //set position
        camera.translateYProperty().set(y);
        camera.translateZProperty().set(z);
        camera.setNearClip(0.01);
        camera.setFarClip(10000000);
    }
    public void createSun(int x, int y, int z, int radius){

        createSphere sun = new createSphere(radius,x,y,z);
        sun.settexture("../pictures/sun.jpg");
        sun.setillumination("../pictures/sun.jpg");
        sun.spheresetmaterial();
        root.getChildren().add(sun.getobject());
    }
    public void createGround(int x, int y, int z, int depth, int width, int height){
        createBox ground = new createBox(depth,width,height,x,y,z);
        ground.settexture();
        //ground.setillumination();
        ground.boxsetmaterial();
        root.getChildren().add(ground.getobject());
    }
    public void createPad(int x, int y, int z, int depth, int width, int height){
        createBox Pad = new createBox(depth,width,height,x,y,z);
        Pad.setcolourgrey();
        //ground.setillumination();
        Pad.boxsetmaterial();
        root.getChildren().add(Pad.getobject());
    }
    public void createAmbientLight(){
        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);
        subScene.setFill(Color.SKYBLUE);
        root.getChildren().add(light);
    }

    public void movement(){
        subScene.setOnKeyPressed(event ->{
            double angle = Math.toRadians(horizontal_cam);
            double sinangle = Math.sin(-angle);
            double cosangle = Math.cos(angle);

            switch (event.getCode()){ //switch to if statements if you want to do multiple keys at once
                case LEFT:
                    horizontal_cam += 2;
                    matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    event.consume();
                    break;

                case RIGHT:
                    horizontal_cam -= 2;
                    matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    event.consume();
                    break;

                case UP:
                    if(vertical_cam > -90){
                        vertical_cam -= 1;
                        matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    }
                    event.consume();
                    break;

                case DOWN:
                    if(vertical_cam < 90){
                        vertical_cam += 1;
                        matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    }
                    event.consume();
                    break;
            }
        });
        subScene.setOnMouseDragged(event -> {
            if (mouse_x < event.getSceneX()){
                horizontal_cam -= 1;
                matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
            }else if (mouse_x > event.getSceneX()){
                horizontal_cam += 1;
                matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
            }
            if(mouse_y < event.getSceneY() && vertical_cam < 90){
                vertical_cam += 0.5;
                matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
            }else if(mouse_y > event.getSceneY() && vertical_cam > -90){
                vertical_cam -= 0.5;
                matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
            }

            mouse_x = event.getSceneX();
            mouse_y = event.getSceneY();
            event.consume();

        });
    }
    private void matrixRotateNode(Node n, double alf, double bet, double gam){ //https://stackoverflow.com/questions/30145414/rotate-a-3d-object-on-3-axis-in-javafx-properly
        double A11=Math.cos(alf)*Math.cos(gam);                                //change the variable names + reorder
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        if(d!=0d){
            double den=2d*Math.sin(d);
            Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));
        }
    }




}

