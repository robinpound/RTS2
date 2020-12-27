package code;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private double time_elapsed = 0.0;                    //time since launch
    private double MAX_FLIGHT_TIME = 2400.0;              //timeout
    private Rocket rocket = new Rocket();

    public void run_simulation(double[] values) {
        double time_step = values[0];
        double wind_speed = values[1], wind_altitude = values[2], wind_azimuth = values[3];
        double fuel_mass = values[4], dry_mass = values[5];
        double drag_coefficient = values[6], nose_diameter = values[7];
        double engine_thrust = values[8], burn_rate = values[9];
        double altitude = values[10], azimuth = values[11];
        double latitude = values[12], longitude = values[13];

        Environment environment = new EarthEnvironment();
        environment.setState(time_step,wind_speed,wind_altitude,wind_azimuth);

        rocket.set_State(fuel_mass,dry_mass,drag_coefficient,nose_diameter,engine_thrust,
                         burn_rate,altitude,azimuth,latitude,longitude,environment);
        rocket.engine_on = true;

        while (time_elapsed < MAX_FLIGHT_TIME){
            rocket.run(environment);
            time_elapsed += environment.get_Timestep();
            if(environment.get_Altitude(rocket.position) <= 0.0){
                break;
            }
        }
    }
    public Rocket getRocket(){return rocket;}
}
