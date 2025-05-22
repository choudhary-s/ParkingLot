package entity;

import java.util.ArrayList;
import java.util.List;

public class Level {
    int levelNo;
    int initialNoOfSlots;
    ArrayList<Slot> slots;

    public Level(int levelNo, int numOfSlots, List<VehicleType> slotTypes) {
        if (numOfSlots > 0 && (slotTypes == null || slotTypes.isEmpty())) {
            throw new IllegalArgumentException("slotTypes cannot be empty or null if numOfSlots is greater than 0");
        }

        this.levelNo = levelNo;
        this.initialNoOfSlots = numOfSlots;
        this.slots = new ArrayList<>();
        for(int i = 0; i < numOfSlots; i++){
            VehicleType determinedAllowedType;
            if (slotTypes == null || slotTypes.isEmpty()) { // Should not happen if numOfSlots > 0 due to check above, but for safety
                determinedAllowedType = VehicleType.COMPACT; // Default or handle as error
            } else if (i < slotTypes.size()) {
                determinedAllowedType = slotTypes.get(i);
            } else {
                determinedAllowedType = slotTypes.get(slotTypes.size() - 1);
            }
            slots.add(new Slot(i + 1, null, determinedAllowedType));
        }
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getInitialNoOfSlots() {
        return initialNoOfSlots;
    }

    public void setInitialNoOfSlots(int initialNoOfSlots) {
        this.initialNoOfSlots = initialNoOfSlots;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }
}
