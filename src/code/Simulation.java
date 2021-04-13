package code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Simulation {

    private double time_elapsed = 0.0;           //time since launch
    private Environment environment;

    private Rocket rocket = new Rocket();

    public Environment Get_environment(){
        return environment;
    }
    public void run_simulation(HashMap<String, Double> inputs) {
        double time_step = inputs.get("Time Step");
        double simulation_duration = inputs.get("Simulation Duration");
        //double playback_speed = inputs.get("Playback Speed");
        double launch_latitude = inputs.get("Latitude"), launch_longitude = inputs.get("Longitude");
        double launch_altitude = inputs.get("Altitude"), launch_azimuth = inputs.get("Azimuth");
        double wind_speed = inputs.get("Wind Speed"), wind_angle = inputs.get("Wind Angle");
        double fuel_mass = inputs.get("Fuel Mass"), dry_mass = (inputs.get("Hull Mass")+inputs.get("Engine Mass")+inputs.get("Payload Mass"));
        double drag_coefficient = inputs.get("Drag Coefficient"), nose_diameter = inputs.get("Nose Diameter");
        double engine_thrust = inputs.get("Engine Thrust"), burn_rate = inputs.get("Burn Rate");

        environment = new EarthEnvironment();
        System.out.println(launch_latitude + " " + launch_longitude);
        environment.setState(time_step, launch_latitude,launch_longitude,launch_altitude,launch_azimuth,wind_speed,wind_angle);

        rocket.set_State(fuel_mass,dry_mass,drag_coefficient,nose_diameter,engine_thrust,burn_rate,environment);
        rocket.setEngine_on(true);

        while (time_elapsed < simulation_duration){
            rocket.run(environment);
            time_elapsed += environment.get_Timestep();
            if(environment.get_Altitude(rocket.getPosition()) <= 0.0){
                break;
            }
        }
    }
    public Rocket getRocket(){return rocket;}
}
