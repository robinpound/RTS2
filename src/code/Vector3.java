package code;

public class Vector3 {
    double VecX,VecY,VecZ;

    public Vector3 (double VecX, double VecY, double VecZ){
            this.VecX = VecX;
            this.VecY = VecY;
            this.VecZ = VecZ;
        }
    public Vector3 (){
            VecX = 0;
            VecY = 0;
            VecZ = 0;
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
