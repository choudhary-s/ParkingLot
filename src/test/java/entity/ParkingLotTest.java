package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {

    private ParkingLot parkingLot;

    // Helper to create a level with a single type for all slots
    private Level createSingleTypeLevel(int levelNo, int numSlots, VehicleType slotType) {
        List<VehicleType> slotTypes = new ArrayList<>();
        for (int i = 0; i < numSlots; i++) {
            slotTypes.add(slotType);
        }
        return new Level(levelNo, numSlots, slotTypes);
    }
    
    private List<Integer> getSlotIds(ArrayList<Slot> slots) {
        return slots.stream().map(Slot::getId).collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        // Default setup, can be overridden in specific tests if needed
        Level l1 = createSingleTypeLevel(1, 5, VehicleType.COMPACT); // 5 COMPACT slots
        Level l2 = createSingleTypeLevel(2, 5, VehicleType.BUS);     // 5 BUS slots
        Level l3 = createSingleTypeLevel(3, 5, VehicleType.MOTORCYCLE); // 5 MOTORCYCLE slots
        parkingLot = new ParkingLot(Arrays.asList(l1, l2, l3));
    }

    @Test
    @DisplayName("Motorcycle should park in MOTORCYCLE slot")
    void parkMotorcycleInMotorcycleSlot() {
        Level motorLevel = createSingleTypeLevel(1, 1, VehicleType.MOTORCYCLE);
        parkingLot = new ParkingLot(Arrays.asList(motorLevel));
        Bike bike = new Bike("M-001");
        assertDoesNotThrow(() -> parkingLot.parkVehicle(bike));
        assertEquals(1, bike.getLevelParked());
        assertEquals(1, bike.getSlotsOccupied().size());
        assertEquals(1, bike.getSlotsOccupied().get(0).getId());
    }

    @Test
    @DisplayName("Motorcycle should park in COMPACT slot")
    void parkMotorcycleInCompactSlot() {
        Level compactLevel = createSingleTypeLevel(1, 1, VehicleType.COMPACT);
        parkingLot = new ParkingLot(Arrays.asList(compactLevel));
        Bike bike = new Bike("M-002");
        assertDoesNotThrow(() -> parkingLot.parkVehicle(bike));
        assertEquals(1, bike.getLevelParked());
    }

    @Test
    @DisplayName("Motorcycle should park in BUS slot")
    void parkMotorcycleInBusSlot() {
        Level busLevel = createSingleTypeLevel(1, 1, VehicleType.BUS);
        parkingLot = new ParkingLot(Arrays.asList(busLevel));
        Bike bike = new Bike("M-003");
        assertDoesNotThrow(() -> parkingLot.parkVehicle(bike));
        assertEquals(1, bike.getLevelParked());
    }

    @Test
    @DisplayName("Car (COMPACT) should park in COMPACT slot")
    void parkCarInCompactSlot() {
        Level compactLevel = createSingleTypeLevel(1, 2, VehicleType.COMPACT);
        parkingLot = new ParkingLot(Arrays.asList(compactLevel));
        Car car = new Car("C-001"); // Needs 2 slots
        assertDoesNotThrow(() -> parkingLot.parkVehicle(car));
        assertEquals(1, car.getLevelParked());
        assertEquals(2, car.getSlotsOccupied().size());
        assertTrue(getSlotIds(car.getSlotsOccupied()).containsAll(Arrays.asList(1,2)));
    }

    @Test
    @DisplayName("Car (COMPACT) should park in BUS slot")
    void parkCarInBusSlot() {
        Level busLevel = createSingleTypeLevel(1, 2, VehicleType.BUS);
        parkingLot = new ParkingLot(Arrays.asList(busLevel));
        Car car = new Car("C-002"); // Needs 2 slots
        assertDoesNotThrow(() -> parkingLot.parkVehicle(car));
        assertEquals(1, car.getLevelParked());
        assertEquals(2, car.getSlotsOccupied().size());
    }

    @Test
    @DisplayName("Bus should park in BUS slot")
    void parkBusInBusSlot() {
        Level busLevel = createSingleTypeLevel(1, 4, VehicleType.BUS);
        parkingLot = new ParkingLot(Arrays.asList(busLevel));
        Bus bus = new Bus("B-001"); // Needs 4 slots
        assertDoesNotThrow(() -> parkingLot.parkVehicle(bus));
        assertEquals(1, bus.getLevelParked());
        assertEquals(4, bus.getSlotsOccupied().size());
    }

    @Test
    @DisplayName("Car (COMPACT) should FAIL to park in solely MOTORCYCLE slots")
    void failParkCarInMotorcycleSlot() {
        Level motorLevel = createSingleTypeLevel(1, 2, VehicleType.MOTORCYCLE); // 2 MOTORCYCLE slots
        parkingLot = new ParkingLot(Arrays.asList(motorLevel));
        Car car = new Car("C-003"); // Needs 2 slots
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(car));
        assertEquals("No slot to park vehicle: " + car.getVehicleId(), exception.getMessage());
    }

    @Test
    @DisplayName("Bus should FAIL to park in solely COMPACT slots")
    void failParkBusInCompactSlot() {
        Level compactLevel = createSingleTypeLevel(1, 4, VehicleType.COMPACT);
        parkingLot = new ParkingLot(Arrays.asList(compactLevel));
        Bus bus = new Bus("B-002"); // Needs 4 slots
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(bus));
        assertEquals("No slot to park vehicle: " + bus.getVehicleId(), exception.getMessage());
    }

    @Test
    @DisplayName("Bus should FAIL to park in solely MOTORCYCLE slots")
    void failParkBusInMotorcycleSlot() {
        Level motorLevel = createSingleTypeLevel(1, 4, VehicleType.MOTORCYCLE);
        parkingLot = new ParkingLot(Arrays.asList(motorLevel));
        Bus bus = new Bus("B-003"); // Needs 4 slots
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(bus));
        assertEquals("No slot to park vehicle: " + bus.getVehicleId(), exception.getMessage());
    }

    @Test
    @DisplayName("Car fails if contiguous slots are not all compatible")
    void failParkCarWithMixedCompatibilityContiguousSlots() {
        // Level: [COMPACT, MOTORCYCLE, COMPACT] Car needs 2 slots.
        Level mixedLevel = new Level(1, 3, Arrays.asList(VehicleType.COMPACT, VehicleType.MOTORCYCLE, VehicleType.COMPACT));
        parkingLot = new ParkingLot(Arrays.asList(mixedLevel));
        Car car = new Car("C-004"); // Needs 2 slots
        
        // It should not park in C,M nor M,C.
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(car));
        assertEquals("No slot to park vehicle: " + car.getVehicleId(), exception.getMessage());
    }
    
    @Test
    @DisplayName("Car parks if first two slots are COMPACT, third is MOTORCYCLE")
    void parkCarWithSufficientCompatibleContiguousSlots() {
        // Level: [COMPACT, COMPACT, MOTORCYCLE] Car needs 2 slots.
        Level level = new Level(1, 3, Arrays.asList(VehicleType.COMPACT, VehicleType.COMPACT, VehicleType.MOTORCYCLE));
        parkingLot = new ParkingLot(Arrays.asList(level));
        Car car = new Car("C-005"); // Needs 2 slots
        assertDoesNotThrow(() -> parkingLot.parkVehicle(car));
        assertEquals(1, car.getLevelParked());
        assertEquals(2, car.getSlotsOccupied().size());
        assertTrue(getSlotIds(car.getSlotsOccupied()).containsAll(Arrays.asList(1,2)));
    }


    @Test
    @DisplayName("Parking fails when no compatible and contiguous slots are available")
    void failParkWhenNoCompatibleSlots() {
        Level motorLevel = createSingleTypeLevel(1, 1, VehicleType.MOTORCYCLE); // Only 1 M slot
        parkingLot = new ParkingLot(Arrays.asList(motorLevel));
        Car car = new Car("C-006"); // Needs 2 slots
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(car));
        assertEquals("No slot to park vehicle: " + car.getVehicleId(), exception.getMessage());
    }
    
    @Test
    @DisplayName("Parking fails when not enough contiguous compatible slots")
    void failParkWhenNotEnoughContiguousCompatibleSlots() {
        // Level: [BUS, COMPACT, BUS] Bus needs 4 slots.
        Level level = new Level(1, 3, Arrays.asList(VehicleType.BUS, VehicleType.COMPACT, VehicleType.BUS));
        parkingLot = new ParkingLot(Arrays.asList(level));
        Bus bus = new Bus("B-004"); // Needs 4 slots
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(bus));
        assertEquals("No slot to park vehicle: " + bus.getVehicleId(), exception.getMessage());
    }

    @Test
    @DisplayName("Unparking makes slots available for reuse by compatible vehicle")
    void unparkAndReuseSlot() throws Exception {
        Level compactLevel = createSingleTypeLevel(1, 2, VehicleType.COMPACT);
        parkingLot = new ParkingLot(Arrays.asList(compactLevel));
        Car car1 = new Car("C-007"); // Needs 2 slots
        
        // Park Car1
        assertDoesNotThrow(() -> parkingLot.parkVehicle(car1));
        assertEquals(1, car1.getLevelParked());
        assertEquals(2, car1.getSlotsOccupied().size());

        // Unpark Car1
        assertDoesNotThrow(() -> parkingLot.unparkVehicle(car1));
        assertNull(car1.getLevelParked());
        assertTrue(car1.getSlotsOccupied().isEmpty());

        // Check level available slots - should be 2 again
        // This is tricky to check directly from printParkings, better to try parking another car
        
        Car car2 = new Car("C-008"); // Needs 2 slots
        assertDoesNotThrow(() -> parkingLot.parkVehicle(car2), "Should be able to park car2 after car1 unparked");
        assertEquals(1, car2.getLevelParked());
        assertEquals(2, car2.getSlotsOccupied().size(), "Car2 should occupy 2 slots");
        assertTrue(getSlotIds(car2.getSlotsOccupied()).containsAll(Arrays.asList(1,2)), "Car2 should occupy slots 1,2");
    }
    
    @Test
    @DisplayName("Motorcycle parks in first available compatible slot after unparking")
    void unparkAndMotorcycleReusesSlot() throws Exception {
        Level l1 = new Level(1, 3, Arrays.asList(VehicleType.MOTORCYCLE, VehicleType.COMPACT, VehicleType.COMPACT));
        parkingLot = new ParkingLot(Arrays.asList(l1));

        Bike bike1 = new Bike("BikeA");
        Car car1 = new Car("CarA"); // Needs 2 slots (COMPACT, COMPACT)

        // Park BikeA in L1S1 (M)
        parkingLot.parkVehicle(bike1);
        assertEquals(1, bike1.getLevelParked());
        assertEquals(1, bike1.getSlotsOccupied().get(0).getId());

        // Park CarA in L1S2, L1S3 (C, C)
        parkingLot.parkVehicle(car1);
        assertEquals(1, car1.getLevelParked());
        assertTrue(getSlotIds(car1.getSlotsOccupied()).containsAll(Arrays.asList(2,3)));
        
        // Unpark BikeA
        parkingLot.unparkVehicle(bike1);
        assertNull(bike1.getLevelParked());

        Bike bike2 = new Bike("BikeB");
        // L1S1 (M) is now free. BikeB should take it.
        parkingLot.parkVehicle(bike2);
        assertEquals(1, bike2.getLevelParked());
        assertEquals(1, bike2.getSlotsOccupied().get(0).getId(), "BikeB should park in slot 1");
    }

    @Test
    @DisplayName("Full parking lot throws exception")
    void fullParkingLotThrowsException() throws Exception {
        Level level = createSingleTypeLevel(1, 1, VehicleType.MOTORCYCLE);
        parkingLot = new ParkingLot(Arrays.asList(level));
        Bike bike1 = new Bike("M-FULL-1");
        parkingLot.parkVehicle(bike1); // Fill the only slot

        Bike bike2 = new Bike("M-FULL-2");
        Exception exception = assertThrows(Exception.class, () -> parkingLot.parkVehicle(bike2));
        assertEquals("No slot to park vehicle: " + bike2.getVehicleId(), exception.getMessage());
    }
}
