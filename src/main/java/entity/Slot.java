package entity;

public class Slot {
    int id;
    String vehicleId;

    public Slot(int id, String vehicleId) {
        this.id = id;
        this.vehicleId = vehicleId;
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
                '}';
    }
}
