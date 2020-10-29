package code;

import com.sun.javafx.event.EventUtil;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.SceneAntialiasing;

import java.lang.annotation.Target;
import java.security.Key;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AnimationWindow{
    private final int WIDTH = 1000;
    private final int HEIGHT = 600;
    private final int ROCKETHEIGHT = 60;
    private final int ROCKETRADIUS = 5;
    private final PhongMaterial rocketMat = new PhongMaterial();
    private Cylinder rocket = new Cylinder(ROCKETRADIUS,ROCKETHEIGHT);

    private final int movementSpeed = 40;
    private double mouse_x = 0;
    private double mouse_y = 0;
    private double horizontal_cam = 0;
    private double vertical_cam = 0;

    Lock lock = new ReentrantLock();

    private double pebblex = 0;
    private double pebbley = 0;
    private double pebblez = 0;

    public void animationstart(Stage primaryStage, List<List> arraylist2D) throws Exception{

        Group root = new Group();
        root.getChildren().add(rocket);

        Scene scene = new Scene(root, WIDTH, HEIGHT,true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);

        rocket.translateXProperty().set(0);
        rocket.translateYProperty().set(0);
        rocket.translateZProperty().set(0);
        rocketMat.setDiffuseColor(Color.RED);
        //rocketMat.setDiffuseMap(new Image(getClass().getResourceAsStream("texturerocket.png")));

        rocket.setMaterial(rocketMat);


        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(200000000000.0);
        camera.translateZProperty().set(-4000);
        camera.translateYProperty().set(-800);
        camera.translateXProperty().set(0);
        camera.setFieldOfView(55);
        scene.setCamera(camera);

        //Ground
        createBox ground = new createBox(100000,100000,1,0,ROCKETHEIGHT/2,0);
        createBox pad = new createBox(100,100,50,0,(ROCKETHEIGHT/2)-2 ,0);
        //pad.setcolourgreen();

        pad.boxsetmaterial();
        ground.settexture();
        ground.setillumination();
        ground.boxsetmaterial();
        root.getChildren().add(ground.getobject());
        root.getChildren().add(pad.getobject());

        //LIGHTING

        PointLight sun = new PointLight(Color.rgb(222,231,255));
        DoubleProperty sunDistance = new SimpleDoubleProperty(10000000);
        BooleanProperty sunlight = new SimpleBooleanProperty(true);
        sun.translateXProperty().bind(sunDistance.multiply(-0.82));
        sun.translateYProperty().bind(sunDistance.multiply(-0.41));
        sun.translateZProperty().bind(sunDistance.multiply(-0.41));
        sun.lightOnProperty().bind(sunlight);
        root.getChildren().add(sun);


        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            double angle = Math.toRadians(horizontal_cam);
            double sinangle = Math.sin(-angle);
            double cosangle = Math.cos(angle);

            switch (event.getCode()){ //switch to if statements if you want to do multiple keys at once
                case W://forward
                    camera.translateZProperty().set(camera.getTranslateZ() + movementSpeed * cosangle);
                    camera.translateXProperty().set(camera.getTranslateX() + movementSpeed * sinangle);
                    break;

                case S://backwards
                    camera.translateZProperty().set(camera.getTranslateZ() - movementSpeed * cosangle);
                    camera.translateXProperty().set(camera.getTranslateX() - movementSpeed * sinangle);
                    break;

                case A://left
                    camera.translateXProperty().set(camera.getTranslateX() - movementSpeed * cosangle);
                    camera.translateZProperty().set(camera.getTranslateZ() + movementSpeed * sinangle);
                    break;

                case D://right
                    camera.translateXProperty().set(camera.getTranslateX() + movementSpeed * cosangle);
                    camera.translateZProperty().set(camera.getTranslateZ() - movementSpeed * sinangle);
                    break;

                case SPACE://space
                    camera.translateYProperty().set(camera.getTranslateY() - movementSpeed);
                    break;

                case C://down
                    camera.translateYProperty().set(camera.getTranslateY() + movementSpeed);
                    break;

                case LEFT:
                    horizontal_cam += 2;
                    matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    break;

                case RIGHT:
                    horizontal_cam -= 2;
                    matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    break;

                case UP:
                    if(vertical_cam > -90){
                        vertical_cam -= 1;
                        matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    }
                    break;

                case DOWN:
                    if(vertical_cam < 90){
                        vertical_cam += 1;
                        matrixRotateNode(camera,0,Math.toRadians(vertical_cam),Math.toRadians(horizontal_cam));
                    }
                    break;
            }
        });




        //Looking left and right
        primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
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
            //System.out.println(String.format("%.3f",event.getSceneX()));

        });

        primaryStage.setTitle("Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        //ROCKET MOVEMENT

        Task task = new Task<Void>(){
            @Override
            public Void call() throws Exception {

                for (List<Double> current: arraylist2D) {
                    //position
                    rocket.translateXProperty().set(current.get(2) * 2);
                    rocket.translateYProperty().set(-current.get(4) * 2);
                    rocket.translateZProperty().set(current.get(3) * 2);
                    //System.out.println(String.format("%f %f %f", current.get(5), current.get(6), current.get(7)));
                    lock.lock();
                    pebblex = current.get(2) * 2;
                    pebbley = -current.get(4) * 2;
                    pebblez = current.get(3) * 2;
                    lock.unlock();
                    //orientation https://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d

                    double x_orientation = current.get(5);
                    double y_orientation = -current.get(7);
                    double z_orientation = current.get(6);

                    Point3D target_orientation = new Point3D(x_orientation,y_orientation,z_orientation).normalize();        //converting to Point3D
                    Point3D original_orientation = new Point3D(0,-1,0).normalize();                                 //original orientation of rocket
                    double rotation_angle = Math.toDegrees(Math.acos(original_orientation.dotProduct(target_orientation))); // finds angle between the two 3D vectors
                    Point3D rotation_axis = original_orientation.crossProduct(target_orientation).normalize();              // finds axis that is orthogonal (right angles) to both origonal and target orientation vectors

                    rocket.setRotationAxis(rotation_axis);
                    rocket.setRotate(rotation_angle);

                    //Showing fuel
                    try{
                        Thread.sleep(10); //sync with time_step!!
                    }catch(InterruptedException interrupted){
                        if(isCancelled()){
                            updateMessage("Cancelled");
                            break;
                        }
                    }
                }
                return null;
            }
        };

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()){
                case P:
                    createBox pebble = new createBox(10, 10, 10, (int) (pebblex), (int) (pebbley), (int) (pebblez));
                    pebble.setcolourgreen();
                    pebble.boxsetmaterial();
                    try {
                        root.getChildren().add(pebble.getobject());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
            }
        });

        try{
            new AnimationTimer(){
                long time = System.currentTimeMillis();
                @Override
                public void handle(long l) {

                    if (System.currentTimeMillis() - time < 200){

                    }else{
                        lock.lock();
                        createBox pebble = new createBox(15, 15, 15, (int) (pebblex), (int) (pebbley), (int) (pebblez));
                        lock.unlock();
                        pebble.setcolourred();
                        pebble.boxsetmaterial();
                        try {
                            root.getChildren().add(pebble.getobject());
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        time = System.currentTimeMillis();
                    }
                }
            }.start();

            //make Animation timer for the rocket, so that the rocket calls instead of calling the rocket.
            //

        }catch(Exception e){
            System.out.println(e);
        }


        new Thread(task).start();

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
