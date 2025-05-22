import entity.*;

import java.util.Arrays;

import java.util.List; // Required for List

public class Driver {
    public static void main(String[] args) {
        // Level 1: 3 slots - M, C, C
        List<VehicleType> level1SlotTypes = Arrays.asList(VehicleType.MOTORCYCLE, VehicleType.COMPACT, VehicleType.COMPACT);
        Level l1 = new Level(1, 3, level1SlotTypes);

        // Level 2: 4 slots - B, B, M, M
        List<VehicleType> level2SlotTypes = Arrays.asList(VehicleType.BUS, VehicleType.BUS, VehicleType.MOTORCYCLE, VehicleType.MOTORCYCLE);
        Level l2 = new Level(2, 4, level2SlotTypes);
        
        ParkingLot pl = new ParkingLot(Arrays.asList(l1, l2));

        System.out.println("Initial Parking Lot Status:");
        pl.printParkings();
        System.out.println("------");

        Car car1 = new Car("Car1"); // Needs 2 COMPACT/BUS slots
        Car car2 = new Car("Car2"); // Needs 2 COMPACT/BUS slots
        // Car car3 = new Car("Car3"); // Unused
        // Car car4 = new Car("Car4"); // Unused

        Bike bike1 = new Bike("Bike1"); // Needs 1 M/C/B slot
        Bike bike2 = new Bike("Bike2"); // Needs 1 M/C/B slot
        Bike bike3 = new Bike("Bike3"); // Needs 1 M/C/B slot
        // Bike bike4 = new Bike("Bike4"); // Unused

        Bus bus1 = new Bus("Bus1");   // Needs 4 BUS slots
        // Bus bus2 = new Bus("Bus2"); // Unused
        // Bus bus3 = new Bus("Bus3"); // Unused
        // Bus bus4 = new Bus("Bus4"); // Unused

        System.out.println("### Starting Parking Scenarios ###");

        try {
            System.out.println("\nAttempting to park Bike1 (MOTORCYCLE)...");
            pl.parkVehicle(bike1); // Should park in l1, slot 1 (MOTORCYCLE)
            pl.printParkings();
            pl.printParking(bike1);
            System.out.println("------");

            System.out.println("\nAttempting to park Car1 (COMPACT)...");
            pl.parkVehicle(car1); // Should park in l1, slots 2,3 (COMPACT, COMPACT)
            pl.printParkings();
            pl.printParking(car1);
            System.out.println("------");

            System.out.println("\nAttempting to park Bike2 (MOTORCYCLE)...");
            // l1 is full or remaining slots not compatible/contiguous for bike if car took 2,3
            // l2 has M, M. Should park in l2, slot 3 (MOTORCYCLE)
            pl.parkVehicle(bike2);
            pl.printParkings();
            pl.printParking(bike2);
            System.out.println("------");

            System.out.println("\nAttempting to park Bus1 (BUS)...");
            // l1 has no BUS slots. l2 has BUS, BUS (slots 1,2)
            // Bus needs 4 slots, so this should fail as there are only 2 contiguous BUS slots in L2.
            try {
                System.out.println("Trying to park Bus1 (needs 4 BUS slots)...");
                pl.parkVehicle(bus1);
                pl.printParkings();
                pl.printParking(bus1);
            } catch (Exception e) {
                System.out.println("Bus1 parking attempt: " + e.getMessage());
            }
            System.out.println("------");
            
            System.out.println("\nUnparking Car1...");
            pl.unparkVehicle(car1);
            pl.printParkings();
            System.out.println("------");

            System.out.println("\nAttempting to park Bike3 (MOTORCYCLE) in a COMPACT slot (L1 S2 or S3)...");
            // Car1 unparked, l1 slots 2,3 (COMPACT, COMPACT) are now available.
            // Bike is compatible with COMPACT.
            pl.parkVehicle(bike3); // Should park in l1, slot 2 (COMPACT)
            pl.printParkings();
            pl.printParking(bike3);
            System.out.println("------");

            System.out.println("\nCreating a new Bus (BusNew) requiring 2 slots for testing specific scenario.");
            Bus busNew = new Bus("BusNew");
            busNew.setNumOfSlotsNeeded(2); // Manually override for test

            System.out.println("\nAttempting to park BusNew (BUS, needs 2 slots) in L2 S1,S2 (BUS, BUS)...");
            // L2 S1,S2 are BUS type and available.
            pl.parkVehicle(busNew);
            pl.printParkings();
            pl.printParking(busNew);
            System.out.println("------");

            System.out.println("\nAttempting to park Car2 (COMPACT) in L2 S3 (MOTORCYCLE slot)...");
            // L2 S3 is MOTORCYCLE. Car (COMPACT) is not compatible with MOTORCYCLE slot by our rules.
            // L2 S4 is also MOTORCYCLE.
            // So Car2 should not be able to park in L2 S3,S4 if it needed 2 slots.
            // Car2 (COMPACT) needs 2 slots. L1 S1 (M), L1 S3 (C after bike3 took S2). No 2 contiguous compatible.
            // L2 S1,S2 taken by busNew. L2 S3 (M), L2 S4 (M). Car not compatible with M.
            try {
                System.out.println("Trying to park Car2 (needs 2 COMPACT/BUS slots)...");
                pl.parkVehicle(car2);
                pl.printParkings();
                pl.printParking(car2);
            } catch (Exception e) {
                System.out.println("Car2 parking attempt: " + e.getMessage());
            }
            System.out.println("------");


        } catch (Exception ex) {
            System.err.println("An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println("\n### Final Parking Lot Status ###");
        pl.printParkings();
    }
}
