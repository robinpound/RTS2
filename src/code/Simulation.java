package code;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private double time_elapsed = 0.0;                    //time since launch
    private double time_step = 0.1;                       //time between calculations
    private Vector3 wind = new Vector3();

    public void set_state(double time_step, double wind_speed, double wind_angle){
        this.time_step = time_step;

        //wind
        double wind_angleRadians = Math.toRadians(wind_angle);
        wind.setState(wind_speed*Math.cos(wind_angleRadians),wind_speed*Math.sin(wind_angleRadians), 0.0);
    }
    public List<List> run_simulation(double fuel_mass,double dry_mass,double drag_coefficient,double nose_diameter, double engine_thrust, double burn_rate, double altitude,double azimuth){
        Rocket rocket = new Rocket();
        rocket.set_State(fuel_mass,dry_mass,drag_coefficient,nose_diameter,engine_thrust,burn_rate,altitude,azimuth);
        rocket.wind = this.wind;
        rocket.engine_on = true;

        List<List> arraylist2D = new ArrayList<List>();
        List<Double> current = new ArrayList<>();

        while (true){

            rocket.run(time_step);
            time_elapsed += time_step;
            //System.out.printf("%.2f, %.3f, %.3f, %.3f, %.3f%n",time_elapsed, rocket.position.getXVec(), rocket.position.getYVec(), rocket.position.getZVec(), rocket.acceleration.getZVec()); // rocket.velocity.mag()
            if(rocket.position.getZVec() <= 0.0){
                break;
            }
            //record data into list
            current = rocket.get_2DList(time_elapsed);
            arraylist2D.add(current);
        }
        //display_data(arraylist2D);
        return arraylist2D;
    }

    private void display_data (List<List> arraylist2D){
        List<Double> current = new ArrayList<>();
        for(int i = 0; i < arraylist2D.size(); i++){
            current = arraylist2D.get(i);
            for (int j = 0; j < current.size(); j++){
                System.out.print(String.format("%.5f : ",current.get(j)));
            }
            System.out.println();
        }
    }
}
