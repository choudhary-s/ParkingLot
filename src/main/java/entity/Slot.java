package entity;

public class Slot {
    int id;
    String vehicleId;
    private VehicleType allowedType;

    public Slot(int id, String vehicleId, VehicleType allowedType) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.allowedType = allowedType;
    }

    public VehicleType getAllowedType() {
        return allowedType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", allowedType=" + allowedType +
                '}';
    }
}
