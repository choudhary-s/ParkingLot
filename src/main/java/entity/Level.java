package entity;

import java.util.ArrayList;

public class Level {
    int levelNo;
    int initialNoOfSlots;
    ArrayList<Slot> slots;

    public Level(int levelNo, int numOfSlots) {
        this.levelNo = levelNo;
        this.initialNoOfSlots = numOfSlots;
        this.slots = new ArrayList<>();
        for(int i=0;i<numOfSlots;i++){
            slots.add(new Slot(i+1, null));
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
