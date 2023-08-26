package entity;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    ArrayList<Level> levels;

    public ParkingLot(List<Level> levels) {
        this.levels = new ArrayList<>();
        this.levels.addAll(levels);
    }
    public void parkVehicle(Vehicle v)throws Exception{
        int n = v.numOfSlotsNeeded;
        //check n continuous slots
        ArrayList<Slot> occSlots = new ArrayList<>();
        for(Level l: levels){
            ArrayList<Slot> avlSlots = l.getSlots();
            int index = 0;
            Slot prev = avlSlots.get(index);
            occSlots.add(avlSlots.get(index));
            for(int i=index+1;i<avlSlots.size() && occSlots.size()<n;i++){
                Slot curr = avlSlots.get(i);
                if(curr.id==prev.id+1){
                    occSlots.add(curr);
                }
                else{
                    occSlots.clear();
                    occSlots.add(curr);
                    prev = curr;
                }
            }
            if(occSlots.size()==n) {
                for (Slot s : occSlots) {
                    avlSlots.remove(s);
                    s.setVehicleId(v.getVehicleId());
                }
                v.setSlotsOccupied(occSlots);
                v.setLevelParked(l.levelNo);
                break;
            }
            occSlots.clear();
        }

        if(occSlots.size()<n){
            throw new Exception("No slot to park vehicle: "+v.getVehicleId());
        }

    }
    public void unparkVehicle(Vehicle v){
        int levelParked = v.getLevelParked();
        ArrayList<Slot> slots = levels.get(levelParked-1).getSlots();
        v.setLevelParked(null);
        for(Slot s: v.getSlotsOccupied()){
            slots.add(s);
            s.setVehicleId(null);
        }
        v.getSlotsOccupied().clear();
    }
    public void printParkings(){
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
