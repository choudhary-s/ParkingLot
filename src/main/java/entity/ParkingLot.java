package entity;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    ArrayList<Level> levels;

    public ParkingLot(List<Level> levels) {
        this.levels = new ArrayList<>();
        this.levels.addAll(levels);
    }

    private boolean isCompatible(VehicleType vehicleType, VehicleType slotType) {
        if (vehicleType == null || slotType == null) return false; // Safety check
        if (vehicleType == VehicleType.MOTORCYCLE) {
            return slotType == VehicleType.MOTORCYCLE || slotType == VehicleType.COMPACT || slotType == VehicleType.BUS;
        } else if (vehicleType == VehicleType.COMPACT) {
            return slotType == VehicleType.COMPACT || slotType == VehicleType.BUS;
        } else if (vehicleType == VehicleType.BUS) {
            return slotType == VehicleType.BUS;
        }
        return false; // Should not happen for defined vehicle types
    }

    public void parkVehicle(Vehicle v) throws Exception {
        int n = v.numOfSlotsNeeded;
        ArrayList<Slot> potentialSlots = new ArrayList<>();
        boolean vehicleParked = false;

        for (Level l : levels) {
            ArrayList<Slot> availableSlotsInLevel = l.getSlots(); // These are presumably already available

            // Sort slots by ID to ensure contiguous check is reliable
            availableSlotsInLevel.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));

            potentialSlots.clear(); // Reset for each level

            for (int i = 0; i < availableSlotsInLevel.size(); i++) {
                Slot currentSlot = availableSlotsInLevel.get(i);

                // Check compatibility for the first slot in a potential sequence
                if (isCompatible(v.getVehicleType(), currentSlot.getAllowedType())) {
                    potentialSlots.add(currentSlot);

                    // Try to find the rest of the n-1 contiguous and compatible slots
                    for (int j = 1; j < n; j++) {
                        if (i + j < availableSlotsInLevel.size()) {
                            Slot nextPotentialSlot = availableSlotsInLevel.get(i + j);
                            Slot lastAddedSlot = potentialSlots.get(potentialSlots.size() - 1);

                            // Check contiguity and compatibility
                            if (nextPotentialSlot.getId() == lastAddedSlot.getId() + 1 &&
                                isCompatible(v.getVehicleType(), nextPotentialSlot.getAllowedType())) {
                                potentialSlots.add(nextPotentialSlot);
                            } else {
                                potentialSlots.clear(); // Not contiguous or not compatible, break inner loop, restart search
                                break;
                            }
                        } else {
                            potentialSlots.clear(); // Not enough slots left in the level, break inner loop
                            break;
                        }
                    }

                    if (potentialSlots.size() == n) {
                        // Found n contiguous and compatible slots
                        for (Slot s : potentialSlots) {
                            availableSlotsInLevel.remove(s); // Remove from available slots list
                            s.setVehicleId(v.getVehicleId());
                        }
                        v.setSlotsOccupied(new ArrayList<>(potentialSlots)); // Set a copy
                        v.setLevelParked(l.levelNo);
                        vehicleParked = true;
                        break; // Break from iterating through slots in this level
                    } else {
                        // If not enough slots were found starting with currentSlot, clear and continue search
                        potentialSlots.clear();
                    }
                } else {
                    // Current slot is not compatible, so clear potentialSlots (though it should be empty or contain incompatible first slot)
                    potentialSlots.clear();
                }
            }
            if (vehicleParked) {
                break; // Break from iterating through levels
            }
        }

        if (!vehicleParked) {
            throw new Exception("No slot to park vehicle: " + v.getVehicleId());
        }
    }

    public void unparkVehicle(Vehicle v) {
        if (v.getLevelParked() == null || v.getSlotsOccupied().isEmpty()) {
            // Vehicle might not be parked or already unparked
            System.out.println("Vehicle " + v.getVehicleId() + " is not parked or already unparked.");
            return;
        }
        int levelParked = v.getLevelParked();
        // Find the correct Level object. Assuming levelNo is 1-indexed.
        Level actualLevel = null;
        for (Level lvl : levels) {
            if (lvl.getLevelNo() == levelParked) {
                actualLevel = lvl;
                break;
            }
        }

        if (actualLevel == null) {
            System.out.println("Error: Level " + levelParked + " not found for vehicle " + v.getVehicleId());
            return; // Or throw an exception
        }

        ArrayList<Slot> slotsInLevel = actualLevel.getSlots();
        for (Slot s : v.getSlotsOccupied()) {
            s.setVehicleId(null); // Mark as empty
            slotsInLevel.add(s);  // Add back to the level's list of available slots
        }
        // Sort slots by ID after adding them back
        slotsInLevel.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));

        v.setLevelParked(null);
        v.getSlotsOccupied().clear();
    }

    public void printParkings() {
        for(Level l: levels){
            System.out.println("Level "+l.levelNo);
            for(Slot s: l.getSlots()){
                System.out.print(s.id+" ");
            }
            System.out.println();
        }
    }
    public void printParking(Vehicle v){
        System.out.println(v.vehicleId+" is parked in Level: "+v.getLevelParked()+" Slots: "+v.getSlotsOccupied());
    }
}
