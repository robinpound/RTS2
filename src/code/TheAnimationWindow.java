package code;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;

import java.util.List;

public class TheAnimationWindow extends NormalUserInterface{

    private final int ROCKETRADIUS = 10;
    private final int ROCKETHEIGHT = 12*ROCKETRADIUS;
    private final double PEBBLESECS = 2.0;     //number of realtime seconds between pebbles
    private final double PEBBLESIZE = 0.25;    //size of pebbles as fraction of space between them (based on velocity)
    private final double TIMESREALTIME = 5.0;  //speed of animation relative to realtime
    private final PhongMaterial rocketMat = new PhongMaterial();
    private Cylinder rocket = new Cylinder(ROCKETRADIUS,ROCKETHEIGHT);

    private final double movementSpeed = 40000;
    private double mouse_x = 0;
    private double mouse_y = 0;
    private double horizontal_cam = 0;
    private double vertical_cam = 0;
    private boolean drop_pebble = false;

    private Rocket theRocket;
    private List<List<Double>> arraylist2D;
    private double vecX, vecY, vecZ;
    private double radius;

    TheAnimationWindow(int windowHeight, int windowWidth, Stage theStage, Rocket theRocket) {
        super(windowHeight, windowWidth, theStage);
        this.theRocket =theRocket;
        this.arraylist2D = theRocket.get_Arraylist();
    }
    public void SetRocketPosition(){

        vecX = arraylist2D.get(0).get(2);
        vecY = -arraylist2D.get(0).get(4);
        vecZ = arraylist2D.get(0).get(3);
        radius = Math.sqrt(vecX*vecX+vecY*vecY+vecZ*vecZ); //Tells radius of earth bc we launch from surface of earth

        rocket.translateXProperty().set(vecX);
        rocket.translateYProperty().set(vecY);
        rocket.translateZProperty().set(vecZ);
        rocketMat.setDiffuseColor(Color.WHITE);
        rocket.setMaterial(rocketMat);
    }
    public void SetCamera(){
        camera.setNearClip(1.0);
        camera.setFarClip(10*radius);
        camera.translateZProperty().set(vecZ-16*ROCKETHEIGHT);
        camera.translateYProperty().set(vecY-4*ROCKETHEIGHT);
        camera.translateXProperty().set(vecX);
        camera.setFieldOfView(60);
        subScene.setCamera(camera);
    }
    public void SetGround(){
        //Ground
        createSphere ground = new createSphere(radius,0,0,0);
        ground.settexture();
        ground.setillumination();
        ground.spheresetmaterial();
        root.getChildren().add(ground.getobject());
    }
    public void movement(){
        theStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
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

                case SPACE://up
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
        theStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
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
    }
    public void RocketMovement(){
        theStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()){
                case P:
                    drop_pebble = true;
            }
        });

        try{
            AnimationTimer timer = new AnimationTimer(){
                long start_timeNanosec = 0;
                double pebble_timeSecs = 0;
                int pebblex = 0;
                int pebbley = 0;
                int pebblez = 0;
                int array_index = 0;
                @Override
                public void handle(long now) {
                    if(array_index >= arraylist2D.size()){
                        stop();
                        return;
                    }
                    List<Double> current = arraylist2D.get(array_index);
                    if (start_timeNanosec == 0) {
                        start_timeNanosec = now;
                    }
                    if ((double) (now - start_timeNanosec) < (1.0e9/TIMESREALTIME)*current.get(0)) {
                        return;     //don't do anything until enough time's elapsed
                    }
                    while ((double) (now - start_timeNanosec) >= (1.0e9/TIMESREALTIME)*current.get(0)) {
                        array_index += 1;
                        if(array_index >= arraylist2D.size()){
                            stop();
                            return;
                        }

                        if (current.get(0) >= pebble_timeSecs+PEBBLESECS || drop_pebble) {
                            double velX = current.get(8);
                            double velY = current.get(9);
                            double velZ = current.get(10);
                            int rr = 1 + (int) (PEBBLESIZE*PEBBLESECS*Math.sqrt(velX*velX+velY*velY+velZ*velZ));
                            createBox pebble = new createBox(rr, rr, rr, (int) pebblex, (int) pebbley, (int) pebblez);
                            if (current.get(1) > 0) {
                                pebble.setcolourgreen();
                            } else {
                                pebble.setcolourred();
                            }
                            pebble.boxsetmaterial();
                            try {
                                root.getChildren().add(pebble.getobject());
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            //keep new position
                            pebblex = current.get(2).intValue();
                            pebbley = -current.get(4).intValue();
                            pebblez = current.get(3).intValue();
                            pebble_timeSecs = current.get(0);
                            drop_pebble = false;
                        }
                        current = arraylist2D.get(array_index);
                    }

                    //position
                    rocket.translateXProperty().set(current.get(2));
                    rocket.translateYProperty().set(-current.get(4));
                    rocket.translateZProperty().set(current.get(3));
                    //System.out.println(String.format("%f %f %f", current.get(5), current.get(6), current.get(7)));

                    //orientation https://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d

                    double x_orientation = current.get(5);
                    double y_orientation = -current.get(7);
                    double z_orientation = current.get(6);

                    Point3D target_orientation = new Point3D(x_orientation,y_orientation,z_orientation).normalize();        //converting to Point3D
                    Point3D original_orientation = new Point3D(0,-1,0).normalize();                               //original orientation of rocket
                    double rotation_angle = Math.toDegrees(Math.acos(original_orientation.dotProduct(target_orientation))); // finds angle between the two 3D vectors
                    Point3D rotation_axis = original_orientation.crossProduct(target_orientation).normalize();              // finds axis that is orthogonal (right angles) to both origonal and target orientation vectors

                    rocket.setRotationAxis(rotation_axis);
                    rocket.setRotate(rotation_angle);
                }
            };
            timer.start();
        }catch(Exception e){
            System.out.println(e);
        }
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
