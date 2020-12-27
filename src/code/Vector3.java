package code;

public class Vector3 {
    private double VecX,VecY,VecZ;

    public Vector3 (double VecX, double VecY, double VecZ){
        setState(VecX, VecY, VecZ);
    }
    public Vector3 (double altitude, double azimuth){
        setState(altitude, azimuth);
    }
    public Vector3 (){
        setState(0, 0, 0);
    }

    public double getXVec(){ return VecX; }
    public double getYVec(){ return VecY; }
    public double getZVec(){ return VecZ; }

    public void addXVec(double amount){ VecX = VecX + amount;}
    public void addYVec(double amount){ VecY = VecY + amount;}
    public void addZVec(double amount){ VecZ = VecZ + amount;}

    public void setXVec(double amount){ VecX = amount;}
    public void setYVec(double amount){ VecY = amount;}
    public void setZVec(double amount){ VecZ = amount;}

    public void setState(double VecX, double VecY, double VecZ){
        this.VecX = VecX;
        this.VecY = VecY;
        this.VecZ = VecZ;
    }

    public void setState(Vector3 vec){
        VecX = vec.getXVec();
        VecY = vec.getYVec();
        VecZ = vec.getZVec();
    }

    public void setState(double altitude, double azimuth) {
        double vecX = Math.cos(Math.toRadians(azimuth));
        double vecY = Math.sin(Math.toRadians(azimuth));
        double vecZ = Math.sin(Math.toRadians(altitude));
        double sclXY = Math.cos(Math.toRadians(altitude));
        setState(vecX*sclXY, vecY*sclXY, vecZ);
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
