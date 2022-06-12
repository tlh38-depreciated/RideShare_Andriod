package net.rideshare_ptc;

public class Car {

    Integer carID;
    String carMake;
    String carModel;
    String carColor;
    String carPlateNum;
    Byte CarIsActive;

    public Car() {
    }

    public Integer getCarID() {
        return carID;
    }

    public void setCarID(Integer carID) {
        this.carID = carID;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPlateNum() {
        return carPlateNum;
    }

    public void setCarPlateNum(String carPlateNum) {
        this.carPlateNum = carPlateNum;
    }

    public Byte getCarIsActive() {
        return CarIsActive;
    }

    public void setCarIsActive(Byte carIsActive) {
        CarIsActive = carIsActive;
    }

    @Override
    public String toString() {
        return "Car Details" +
                "\n carMake='" + carMake +
                "\n carModel='" + carModel +
                "\n carColor='" + carColor  +
                "\n carPlateNum='" + carPlateNum;
    }
}
