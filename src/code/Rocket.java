package code;
import java.util.ArrayList;
import java.util.List;

public class Rocket {

    public Vector3 position = new Vector3();    //Position of rocket
    public Vector3 orientation = new Vector3(); //Orientation of rocket
    public Vector3 velocity = new Vector3();    //Velocity of rocket
    public Vector3 acceleration = new Vector3();//Acceleration of rocket
    public Vector3 thrust = new Vector3();      //Thrust of rocket - it's the force(N) in:  F = ma
    public Vector3 drag = new Vector3();        //Drag(N) of rocket    (3.14 * atmospheric_density * drag_coefficient * (nose_diameter)^2)/8
    public Vector3 wind = new Vector3();

    public double fuel_mass = 0;                //mass(Kg) of rocket
    public double dry_mass = 0;                 //dry_mass (Kg) of rocket
    public double drag_factor = 0;              //How un-aerodynamic the rocket nose is (?) e.g. ?
    public double nose_diameter = 0;            //How large the rocket nose diameter is (m)
    public double engine_thrust = 0;            //Engine thrust(N) of rocket engine
    public double burn_rate = 0;                //fuel used (Kg/second)

    public boolean engine_on = false;           //is engine burning (engine will only burn if this is true && enough fuel)


    public void set_State(double fuel_mass,     double dry_mass,  double drag_coefficient, double nose_diameter,
                          double engine_thrust, double burn_rate, double altitude,         double azimuth){

        //initial settings (Things that shouldn't be 0 at the start)
        this.fuel_mass = fuel_mass;
        this.dry_mass = dry_mass;
        this.drag_factor = drag_coefficient*nose_diameter*nose_diameter*Math.PI/8.0;
        this.nose_diameter = nose_diameter;
        this.engine_thrust = engine_thrust;
        this.burn_rate = burn_rate;

        //getting the orientation vector from angles
        double x0 = Math.cos(Math.toRadians(azimuth));
        double y0 = Math.sin(Math.toRadians(azimuth));
        double z0 = Math.sin(Math.toRadians(altitude));
        double scale = Math.cos(Math.toRadians(altitude)); //k
        this.orientation.setState(x0*scale, y0*scale, z0);

        //if the rocket is used twice, this is to make sure ALL values are set back to normal
        position.setState(0,0,0);
        //orientation.setState(0,0,0);      is done differently above!
        velocity.setState(0,0,0);
        acceleration.setState(0,0,0);
        thrust.setState(0,0,0);
        drag.setState(0,0,0);
        engine_on = false;
    }

    /*
    public void set_Fuel_mass(double fuel_mass) {
        this.fuel_mass = fuel_mass;
    }
    public void set_Dry_mass(double dry_mass) {
        this.dry_mass = dry_mass;
    }
    public void set_Drag_coefficient(double drag_coefficient, double nose_diameter) {
        this.nose_diameter = nose_diameter;
        this.drag_factor = drag_coefficient*nose_diameter*nose_diameter*Math.PI/8.0;
    }
    public void set_Engine_thrust(double engine_thrust) {
        this.engine_thrust = engine_thrust;
    }
     */
    public List<Double> get_2DList(double time_elapsed){
        List<Double> current = new ArrayList<>();
        current.add(time_elapsed);
        current.add(fuel_mass);
        current.add(position.getXVec());
        current.add(position.getYVec());
        current.add(position.getZVec());
        current.add(orientation.getXVec());
        current.add(orientation.getYVec());
        current.add(orientation.getZVec());
        current.add(velocity.getXVec());
        current.add(velocity.getYVec());
        current.add(velocity.getZVec());
        current.add(acceleration.getXVec());
        current.add(acceleration.getYVec());
        current.add(acceleration.getZVec());
        current.add(thrust.getXVec());
        current.add(thrust.getYVec());
        current.add(thrust.getZVec());
        current.add(drag.getXVec());
        current.add(drag.getYVec());
        current.add(drag.getZVec());
        current.add(wind.getXVec());
        current.add(wind.getYVec());
        current.add(wind.getZVec());

        return current;
    }

    private double calculate_AtmosphericDensity(){
         /*
         convert height to atmospheric density using approximate rules from NASA:
         https://www.grc.nasa.gov/www/k-12/airplane/atmosmet.html
         */

        double height = position.getZVec();
        double T, p; // temperature, pressure

        if (height > 25000.0) {                   // upper stratosphere
            T = -131.21 + 0.00299*height;
            p = 2.488 * Math.pow((T + 273.1) / 216.6, -11.388);
        } else if (height > 11000.0) {            // lower stratosphere
            T = -56.46;
            p = 22.65 * Math.exp(1.73 - 0.000157 * height);
        } else {                                    // troposphere
            T = 15.04 - 0.00649 * height;
            p = 101.29 * Math.pow((T + 273.1) / 288.08, 5.256);
        }
        double density = p / (0.2869 * (T + 273.1));

        return density;
    }
    private Vector3 calculate_Gravity(){
         /*
         convert height to atmospheric density using nominal values for surface gravity and equatorial radius from NASA:
         https://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
         */
        double height = position.getZVec();
        Vector3 gravity = new Vector3(0,0,9.798 * (1.0 - height / 6378137.0));
        return gravity;
    }

    public void run(double time_step){
        double fuel_used;

        //Calculate thrust --  is burning, enough fuel, then thrust
        if (engine_on && (fuel_mass >= (0.5 * burn_rate * time_step))){
            thrust = orientation.times(engine_thrust);
            fuel_used = burn_rate * time_step;
        }else{ //else thrust = 0
            thrust.setState(0,0,0);
            fuel_used = 0;
        }

        //Calculate drag --
        Vector3 atmospheric_velocity = velocity.sub(wind);
        drag = atmospheric_velocity.times(drag_factor * calculate_AtmosphericDensity() * atmospheric_velocity.mag());
        //Calculate acceleration --
        acceleration = thrust.sub(drag).div(dry_mass + fuel_mass).sub(calculate_Gravity());
        //integrate velocity for position
        position = position.add(velocity.times(time_step).add(acceleration.times(time_step*time_step/2))); //velocity here is talking about the previous velocity (initial velocity)
        //integrate acceleration for velocity
        velocity = velocity.add(acceleration.times(time_step));

        //check for hitting ground
        if (position.VecZ <= 0.0){
            position.setZVec(0.0);
            velocity.setState(0,0,0);
            acceleration.setState(0,0,0);
        }
        //update orientation according to the direction of velocity
        if (velocity.mag() > Float.MIN_VALUE) {
            orientation = velocity.div(velocity.mag());
        }
        //update fuel
        fuel_mass = Math.max(0.0 , fuel_mass-fuel_used);
    }
}

