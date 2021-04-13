package code;
import java.util.ArrayList;
import java.util.List;

public class Rocket {

    private Vector3 position = new Vector3();    //Position of rocket
    private Vector3 orientation = new Vector3(); //Orientation of rocket
    private Vector3 velocity = new Vector3();    //Velocity of rocket
    private Vector3 acceleration = new Vector3();//Acceleration of rocket
    private Vector3 thrust = new Vector3();      //Thrust of rocket - it's the force(N) in:  F = ma
    private Vector3 drag = new Vector3();        //Drag(N) of rocket (3.14 * atmospheric_density * drag_coefficient * (nose_diameter)^2)/8

    private double time_elapsed = 0;             //flight time
    private double fuel_mass = 0;                //mass(Kg) of rocket
    private double dry_mass = 0;                 //dry_mass (Kg) of rocket
    private double drag_factor = 0;              //How un-aerodynamic the rocket nose is (?)
    private double nose_diameter = 0;            //How large the rocket nose diameter is (m)
    private double engine_thrust = 0;            //Engine thrust(N) of rocket engine
    private double burn_rate = 0;                //fuel used (Kg/second)

    private boolean engine_on = false;           //is engine burning (engine will only burn if this is true && enough fuel)

    List<List<Double>> arraylist = new ArrayList<>(); // 2D list of results

    public void set_State(double fuel_mass,     double dry_mass,    double drag_coefficient,    double nose_diameter,
                          double engine_thrust, double burn_rate,   Environment environment){

        //initial settings (Things that shouldn't be 0 at the start)
        this.fuel_mass = fuel_mass;
        this.dry_mass = dry_mass;
        this.drag_factor = drag_coefficient*nose_diameter*nose_diameter*Math.PI/8.0;
        this.nose_diameter = nose_diameter;
        this.engine_thrust = engine_thrust;
        this.burn_rate = burn_rate;

        //if the rocket is used twice, this is to make sure ALL values are set back to normal
        position.setState(environment.get_LaunchPosition());
        orientation.setState(environment.get_LaunchOrientation(position));
        velocity.setState(0,0,0);
        acceleration.setState(0,0,0);
        thrust.setState(0,0,0);
        drag.setState(0,0,0);
        arraylist.clear();
        engine_on = false;
        time_elapsed = 0;
    }

    public void update_Arraylist(Environment environment){
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

        Vector3 wind = environment.get_Wind(position);
        current.add(wind.getXVec());
        current.add(wind.getYVec());
        current.add(wind.getZVec());

        Vector3 gravity = environment.get_Gravity(position);
        current.add(gravity.getXVec());
        current.add(gravity.getYVec());
        current.add(gravity.getZVec());

        double atmospheric_density = environment.get_AtmosphericDensity(position);
        current.add(atmospheric_density);

        arraylist.add(current);
    }
    public void run(Environment environment){
        double fuel_used;
        double time_step = environment.get_Timestep();

        //add launch condition at start of 2D array
        if (arraylist.isEmpty()) {
            update_Arraylist(environment);
        }

        //Calculate thrust --  is burning, enough fuel, then thrust
        if (engine_on && (fuel_mass >= (0.5 * burn_rate * time_step))){
            thrust = orientation.times(engine_thrust);
            fuel_used = burn_rate * time_step;
        }else{ //else thrust = 0
            thrust.setState(0,0,0);
            fuel_used = fuel_mass;
        }

        //Calculate drag --
        Vector3 atmospheric_velocity = velocity.sub(environment.get_Wind(position));
        Vector3 gravity = environment.get_Gravity(position);
        drag = atmospheric_velocity.times(drag_factor * environment.get_AtmosphericDensity(position) * atmospheric_velocity.mag());
        //Calculate acceleration --
        acceleration = thrust.sub(drag).div(dry_mass + fuel_mass).sub(gravity);
        //integrate velocity for position
        position = position.add(velocity.times(time_step).add(acceleration.times(time_step*time_step*0.5))); //velocity here is talking about the previous velocity (initial velocity)
        //integrate acceleration for velocity
        velocity = velocity.add(acceleration.times(time_step));

        //check for hitting ground
        if (environment.get_Altitude(position) <= 0.0){
            velocity.setState(0,0,0);
            acceleration.setState(0,0,0);
        }
        //update orientation according to the direction of velocity
        if (velocity.mag() > Float.MIN_VALUE) {
            orientation = velocity.div(velocity.mag());
        }
        //update fuel
        fuel_mass = Math.max(0.0 , fuel_mass-fuel_used);
        //update time
        time_elapsed += time_step;
        //update 2D array
        update_Arraylist(environment);
    }
    public List<List<Double>> get_Arraylist() {
        return arraylist;
    }
    public void setEngine_on(Boolean b){
        engine_on = b;
    }
    public Vector3 getPosition(){
        return position;
    }
}
