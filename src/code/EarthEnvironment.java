package code;

public class EarthEnvironment extends Environment {

    public EarthEnvironment () {
        set_Planetradius(6378137.0);
        set_Timestep(0.01);
    }

    public double get_AtmosphericDensity(PlanetCoordinates location){
         /*
         convert height to atmospheric density using approximate rules from NASA:
         https://www.grc.nasa.gov/www/k-12/airplane/atmosmet.html
         */

        double height = get_Altitude(location);
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

        return p / (0.2869 * (T + 273.1));
    }

    public Vector3 get_Gravity(PlanetCoordinates location){
         /*
         convert height to atmospheric density using nominal values for surface gravity and equatorial radius from NASA:
         https://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
         */
        double height = get_Altitude(location);
        double magnitude = 9.798 * (1.0 - height / planetradius);
        Vector3 direction = location.getVector3();
        direction = direction.div(direction.mag());
        return direction.times(magnitude);
    }
}

