package code;

//calculations that can be the same for all planets
abstract class Environment {

    protected double planet_radius = 0;
    protected double time_step = 0;
    protected Vector3 launch_orientation = new Vector3();
    protected Vector3 launch_position = new Vector3();
    protected Vector3 wind_velocity = new Vector3();

    abstract public Vector3 get_Gravity(PlanetCoordinates coordinates);

    abstract public double get_AtmosphericDensity(PlanetCoordinates coordinates);

    public PlanetCoordinates get_SurfaceCoordinates(double latitude, double longitude) {
        return new PlanetCoordinates(planet_radius, latitude, longitude);
    }

    public double get_Altitude(PlanetCoordinates coordinates) {
        return Math.max(0.0, coordinates.getRadius()- planet_radius);
    }

    public double get_Latitude(PlanetCoordinates coordinates) {
        return coordinates.getLatitude();
    }

    public double get_Longitude(PlanetCoordinates coordinates) {
        return coordinates.getLongitude();
    }

    public Vector3 get_Gravity(Vector3 location) {
        PlanetCoordinates coordinates = new PlanetCoordinates(location);
        return get_Gravity(coordinates);
    }

    public double get_AtmosphericDensity(Vector3 location) {
        return get_AtmosphericDensity(new PlanetCoordinates(location));
    }

    public double get_Altitude(Vector3 location) { return get_Altitude(new PlanetCoordinates(location)); }

    public double get_Latitude(Vector3 location) {
        return get_Latitude(new PlanetCoordinates(location));
    }

    public double get_Longitude(Vector3 location) {
        return get_Longitude(new PlanetCoordinates(location));
    }

    public Vector3 get_LaunchPosition() {
        return launch_position;
    }

    private Vector3 rotate_VectorToCoordinates(Vector3 vector, PlanetCoordinates coordinates) {
        double latitude = Math.toRadians(coordinates.getLatitude());
        double cos_latitude = Math.cos(latitude);
        double sin_latitude = Math.sin(latitude);

        double longitude = Math.toRadians(coordinates.getLongitude());
        double cos_longitude = Math.cos(longitude);
        double sin_longitude = Math.sin(longitude);

        // wind vector at zero latitude & longitude
        double vecX = vector.getXVec();
        double vecY = vector.getYVec();
        double vecZ = vector.getZVec();

        // rotate vector to desired latitude
        double latX = vecX*cos_latitude-vecZ*sin_latitude;
        double latZ = vecX*sin_latitude+vecZ*cos_latitude;

        // rotate vector to desired longitude
        double lngX = latX*cos_longitude-vecY*sin_longitude;
        double lngY = latX*sin_longitude+vecY*cos_longitude;

        return new Vector3(lngX, lngY, latZ);
    }

    public Vector3 get_LaunchOrientation(Vector3 location) {
        PlanetCoordinates coordinates = new PlanetCoordinates(location);
        return rotate_VectorToCoordinates(launch_orientation, coordinates);
    }

    public Vector3 get_Wind(Vector3 location) {
        PlanetCoordinates coordinates = new PlanetCoordinates(location);
        return rotate_VectorToCoordinates(wind_velocity, coordinates);
    }

    public double get_Timestep() {
        return time_step;
    }

    //set wind vector at zero latitude & longitude
    public void set_Wind(double wind_speed, double wind_angle)
    {
        double vecX = 0.0;
        double vecY = wind_speed*Math.sin(Math.toRadians(wind_angle));
        double vecZ = wind_speed*Math.cos(Math.toRadians(wind_angle));
        wind_velocity.setState(vecX, vecY, vecZ);
    }

    public void set_Timestep(double timestep) {
        this.time_step = timestep;
    }

    //set launch orientation vector at zero latitude & longitude
    public void set_LaunchOrientation(double altitude, double azimuth) {
        launch_orientation.setState(altitude, azimuth);
    }

    //set launch position vector from latitude and longitude
    public void set_LaunchPosition(double latitude, double longitude)
    {
        launch_position.setStateFromCoordinates(planet_radius, latitude, longitude);
    }

    protected void set_Planetradius(double planetradius) { this.planet_radius = planetradius; }

    public void setState(double timestep, double latitude, double longitude, double altitude, double azimuth,
                         double wind_speed, double wind_angle) {
        set_Timestep(timestep);
        set_LaunchPosition(latitude, longitude);
        set_LaunchOrientation(altitude, azimuth);
        set_Wind(wind_speed, wind_angle);
    }
}
