package code;

abstract class Environment {

    protected double planetradius, timestep;
    protected Vector3 orientation = new Vector3();
    protected Vector3 position = new Vector3();
    protected Vector3 wind = new Vector3();

    abstract public Vector3 get_Gravity(PlanetCoordinates location);

    abstract public double get_AtmosphericDensity(PlanetCoordinates location);

    public PlanetCoordinates get_SurfaceCoordinates(double latitude, double longitude) {
        return new PlanetCoordinates(planetradius, latitude, longitude);
    }

    public double get_Altitude(PlanetCoordinates location) {
        return Math.max(0.0, location.getRadius()-planetradius);
    }

    public double get_Latitude(PlanetCoordinates location) {
        return location.getLatitude();
    }

    public double get_Longitude(PlanetCoordinates location) {
        return location.getLongitude();
    }

    public Vector3 get_Gravity(Vector3 location) {
        PlanetCoordinates pc = new PlanetCoordinates(location);
        return get_Gravity(pc);
    }

    public double get_AtmosphericDensity(Vector3 location) {
        return get_AtmosphericDensity(new PlanetCoordinates(location));
    }

    public double get_Altitude(Vector3 location)
    {
        return get_Altitude(new PlanetCoordinates(location));
    }

    public double get_Latitude(Vector3 location) {
        return get_Latitude(new PlanetCoordinates(location));
    }

    public double get_Longitude(Vector3 location) {
        return get_Longitude(new PlanetCoordinates(location));
    }

    public Vector3 get_Orientation() {
        return orientation;
    }

    public Vector3 get_Position() {
        return position;
    }

    public Vector3 get_Wind() {
        return wind;
    }

    public double get_Timestep() {
        return timestep;
    }

    public void set_Wind(Vector3 wind) {
        this.wind.setState(wind);
    }

    public void set_Wind(double wind_speed, double wind_altitude, double wind_azimuth) {
        Vector3 wind_orientation = new Vector3(wind_altitude, wind_azimuth);
        this.wind.setState(wind_orientation.times(wind_speed));
    }

    public void set_Timestep(double timestep) {
        this.timestep = timestep;
    }

    protected void set_Planetradius(double planetradius) { this.planetradius = planetradius; }

    public void setState(double timestep, double wind_speed, double wind_altitude, double wind_azimuth) {
        set_Timestep(timestep);
        set_Wind(wind_speed, wind_altitude, wind_azimuth);
    }
}
