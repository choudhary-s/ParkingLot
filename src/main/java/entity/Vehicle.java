package entity;

import java.util.ArrayList;

public abstract class Vehicle {
    String vehicleId;
    VehicleType vehicleType;
    int numOfSlotsNeeded;
    Integer levelParked;
    ArrayList<Slot> slotsOccupied;

    public Vehicle(String vehicleId, VehicleType vehicleType) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.slotsOccupied = new ArrayList<>();
        switch (vehicleType){
            case MOTORCYCLE:
                numOfSlotsNeeded = 1;
                break;
            case BUS:
                numOfSlotsNeeded = 4;
                break;
            default:
                numOfSlotsNeeded = 2;
        }
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getNumOfSlotsNeeded() {
        return numOfSlotsNeeded;
    }

    public void setNumOfSlotsNeeded(int numOfSlotsNeeded) {
        this.numOfSlotsNeeded = numOfSlotsNeeded;
    }

    public Integer getLevelParked() { // Changed return type to Integer
        return levelParked;
    }

    public void setLevelParked(Integer levelParked) {
        this.levelParked = levelParked;
    }

    public ArrayList<Slot> getSlotsOccupied() {
        return slotsOccupied;
    }

    public void setSlotsOccupied(ArrayList<Slot> slotsOccupied) {
        this.slotsOccupied.addAll(slotsOccupied);
    }
}
