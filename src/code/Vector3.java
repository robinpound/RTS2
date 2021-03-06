package code;

public class Vector3 {
    private double VecX,VecY,VecZ;

    //make zero vector
    public Vector3 (){
        setState(0, 0, 0);
    }

    //make vector with chosen coordinates
    public Vector3 (double VecX, double VecY, double VecZ){
        setState(VecX, VecY, VecZ);
    }

    //make unit vector with required altitude and azimuth
    public Vector3 (double altitude, double azimuth) {
        setState(altitude, azimuth);
    }

    public double getXVec(){ return VecX; }
    public double getYVec(){ return VecY; }
    public double getZVec(){ return VecZ; }

    //create vector from separate xyz coordinates
    public void setState(double VecX, double VecY, double VecZ){
        this.VecX = VecX;
        this.VecY = VecY;
        this.VecZ = VecZ;
    }

    //create a copy of another vector
    public void setState(Vector3 vec){
        VecX = vec.getXVec();
        VecY = vec.getYVec();
        VecZ = vec.getZVec();
    }

    //set vector to unit amplitude with specified altitude and azimuth angles
    public void setState(double altitude, double azimuth) {
        double vecY = Math.sin(Math.toRadians(azimuth));
        double vecZ = Math.cos(Math.toRadians(azimuth));
        double vecX = Math.sin(Math.toRadians(altitude));
        double sclX = Math.cos(Math.toRadians(altitude));
        setState(vecX, vecY*sclX, vecZ*sclX);
    }

    //set vector using radius, latitude and longitude
    public void setStateFromCoordinates(double radius, double latitude, double longitude) {
        double vecX = Math.cos(Math.toRadians(longitude));
        double vecY = Math.sin(Math.toRadians(longitude));
        double vecZ = Math.sin(Math.toRadians(latitude));
        double sclZ = radius*Math.cos(Math.toRadians(latitude));
        setState(vecX*sclZ, vecY*sclZ, vecZ*radius);
    }

    public Vector3 times(double scalar){
        return new Vector3(VecX * scalar, VecY * scalar, VecZ * scalar);
    }
    public Vector3 div(double scalar){
        return new Vector3(VecX / scalar, VecY / scalar, VecZ / scalar);
    }
    public Vector3 add(Vector3 vec){
        return new Vector3(VecX + vec.VecX, VecY + vec.VecY, VecZ + vec.VecZ);
    }
    public Vector3 sub(Vector3 vec){
        return new Vector3(VecX - vec.VecX, VecY - vec.VecY, VecZ - vec.VecZ);
    }
    public double mag(){
        return Math.sqrt(VecX*VecX + VecY*VecY + VecZ*VecZ);
    }


}
