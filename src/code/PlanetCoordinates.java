package code;

// class for geographical representation of 3-d vectors
// radius: distance from origin (never negative)
// longitude: angle in degrees (between -180 and +180) relative to meridian, East = positive, West = negative
// latitude: angle in degrees (between -90 and +90) relative to equator, North = positive, South = negative

public class PlanetCoordinates {
    double radius, latitude, longitude;
    Vector3 vector3;

    public PlanetCoordinates() {
        setState(0.0, 0.0, 0.0);
    }
    public PlanetCoordinates(double radius, double latitude, double longitude) {
        setState(radius, latitude, longitude);
    }
    public PlanetCoordinates(Vector3 vector3) {
        setState(vector3);
    }

    public void setRadius(double radius) {
        if (Double.isNaN(radius) || Double.isNaN(latitude) || Double.isNaN(longitude) || radius < 0.0) {
            this.radius = Double.NaN;
            latitude = Double.NaN;
            longitude = Double.NaN;
        } else {
            this.radius = radius;
        }
    }

    public void setLatitude(double latitude) {
        if (Double.isNaN(radius) || Double.isNaN(latitude) || Double.isNaN(longitude) || latitude < -90.0 || latitude > 90.0) {
            radius = Double.NaN;
            this.latitude = Double.NaN;
            longitude = Double.NaN;
        } else {
            this.latitude = latitude;
        }
    }

    public void setLongitude(double longitude) {
        if (Double.isNaN(radius) || Double.isNaN(latitude) || Double.isNaN(longitude) || longitude < -180.0 || longitude > 180.0) {
            radius = Double.NaN;
            latitude = Double.NaN;
            this.longitude = Double.NaN;
        } else {
            this.longitude = longitude;
        }
    }

    public void setState(double radius, double latitude, double longitude) {
        if (radius < 0.0 || latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
            this.radius = Double.NaN;
            this.latitude = Double.NaN;
            this.longitude = Double.NaN;
        } else {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public void setState(Vector3 vector3) {
        double vecx = vector3.getXVec();
        double vecy = vector3.getYVec();
        double vecz = vector3.getZVec();

        double radius = vector3.mag();
        double latitude = Math.toDegrees(Math.asin(vecz/radius));
        double longitude = Math.toDegrees(Math.atan2(vecy, vecx));

        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Vector3 getVector3() {
        if (Double.isNaN(radius) || Double.isNaN(latitude) || Double.isNaN(longitude)) {
            return new Vector3(Double.NaN, Double.NaN, Double.NaN);
        }

        double vecX = Math.cos(Math.toRadians(longitude));
        double vecY = Math.sin(Math.toRadians(longitude));
        double vecZ = Math.sin(Math.toRadians(latitude));
        double sclXY = Math.cos(Math.toRadians(latitude))*radius;

        return new Vector3(vecX*sclXY, vecY*sclXY, vecZ*radius);
    }
}
