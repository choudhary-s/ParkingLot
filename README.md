# ParkingLot

This project is a command-line Java application that simulates a parking lot system. It allows users to define a parking lot with multiple levels and varying numbers of slots per level. The system can then park and unpark different types of vehicles (Cars, Bikes, Buses), each requiring a specific number of slots. The application demonstrates core parking lot functionalities like finding available slots, allocating them to vehicles, and releasing them when vehicles unpark. The `Driver.java` class serves as the main entry point, showcasing example usage of the parking lot simulation.

## Core Entities

The simulation is built around the following core entities:

*   **`Vehicle`**: An abstract class representing a generic vehicle.
    *   Properties: `vehicleId` (String), `vehicleType` (VehicleType), `numOfSlotsNeeded` (int), `levelParked` (Integer), `slotsOccupied` (ArrayList<Slot>).
    *   Subclasses: `Car`, `Bike`, `Bus`. Each subclass initializes the `vehicleType` and `numOfSlotsNeeded` based on the vehicle type.
        *   `Car`: Represents a car, typically needing 2 slots.
        *   `Bike`: Represents a motorcycle, typically needing 1 slot.
        *   `Bus`: Represents a bus, typically needing 4 slots.
*   **`VehicleType`**: An enum defining the types of vehicles the parking lot can accommodate (`COMPACT`, `MOTORCYCLE`, `BUS`).
*   **`Slot`**: Represents a single parking slot.
    *   Properties: `id` (int), `vehicleId` (String - null if the slot is empty), `allowedType` (VehicleType).
    *   Responsibilities: Holds information about its own ID, whether it's occupied, and what type of vehicle it is primarily designated for.
*   **`Level`**: Represents a level within the parking lot.
    *   Properties: `levelNo` (int), `initialNoOfSlots` (int), `slots` (ArrayList<Slot>).
    *   Responsibilities: Manages a collection of slots on that particular level. Initializes a predefined number of slots upon creation, each with a specific `allowedType`. The constructor `Level(int levelNo, int numOfSlots, List<VehicleType> slotTypes)` allows defining the types for each slot. If the `slotTypes` list is shorter than `numOfSlots`, the last type in the list is used for the remaining slots.
*   **`ParkingLot`**: Represents the entire parking lot.
    *   Properties: `levels` (ArrayList<Level>).
    *   Responsibilities:
        *   Manages a collection of levels.
        *   `parkVehicle(Vehicle v)`: Finds contiguous available slots across levels that are compatible with the vehicle's type, allocates them to the vehicle, and updates the vehicle's and slots' statuses. Throws an exception if no suitable slot is found.
        *   `unparkVehicle(Vehicle v)`: Releases the slots occupied by a vehicle and updates their status.
        *   `printParkings()`: Prints the current status of all levels and available slots.
        *   `printParking(Vehicle v)`: Prints the parking details (level and slots) for a specific vehicle.

## Key Functionalities

The parking lot system provides the following key functionalities:

### 1. Parking Vehicles (`parkVehicle(Vehicle v)`)

*   **Slot Requirement**: The method first determines the number of contiguous slots (`n`) needed by the vehicle (`v.numOfSlotsNeeded`).
*   **Type Compatibility**: Crucially, each slot in the potential sequence must be compatible with the vehicle's type (see "Slot Type Restrictions and Compatibility" below).
*   **Iterating Through Levels**: It iterates through each `Level` in the `ParkingLot`.
*   **Searching for Contiguous, Compatible Slots**: Within each level, it iterates through the available slots (which are sorted by ID for reliable contiguity checks).
    *   It attempts to find `n` contiguous slots where *every slot in the sequence* is compatible with the vehicle's type and is currently unoccupied.
    *   A temporary list (`potentialSlots`) stores candidate slots. If a non-contiguous or non-compatible slot is encountered, this list is cleared, and the search for a new sequence restarts from the next suitable slot.
*   **Allocation**:
    *   If `n` contiguous slots are found:
        *   These slots are removed from the level's list of available slots (`avlSlots`).
        *   The `vehicleId` of the parked vehicle is set for each of these occupied slots.
        *   The vehicle's `slotsOccupied` list is updated with these slots.
        *   The vehicle's `levelParked` is set to the current level number.
        *   The search stops, and the vehicle is considered parked.
*   **No Slot Found**: If the iteration completes without finding suitable (i.e., enough contiguous, type-compatible, and available) slots in any level, an exception is thrown, indicating that the vehicle cannot be parked.

### 2. Slot Type Restrictions and Compatibility

A significant feature of this system is the ability to designate slots for specific vehicle types and define rules for which vehicles can park in which slots.

**Entity Changes:**

*   **`Slot.java`**: Each `Slot` object now has an `allowedType` (VehicleType) field, indicating the type of vehicle the slot is primarily intended for.
*   **`Level.java`**: The `Level` constructor (`Level(int levelNo, int numOfSlots, List<VehicleType> slotTypes)`) now accepts a list of `VehicleType`. This list is used to assign an `allowedType` to each slot created within the level.
    *   If the `slotTypes` list is shorter than the number of slots in the level, the last `VehicleType` in the list is used for all subsequent slots.
    *   An `IllegalArgumentException` is thrown if `numOfSlots > 0` but `slotTypes` is null or empty.
*   **`ParkingLot.java`**: The `parkVehicle(Vehicle v)` method now includes logic to check for type compatibility between the vehicle and the potential slots. It uses a helper method `isCompatible(VehicleType vehicleType, VehicleType slotType)` to determine if a vehicle can use a slot. A sequence of slots is only considered valid if all slots in that sequence are compatible with the parking vehicle.

**Compatibility Rules:**

The system enforces the following parking rules based on vehicle type and slot type:

*   **Motorcycle (`MOTORCYCLE`)**: Can park in slots designated as:
    *   `MOTORCYCLE`
    *   `COMPACT`
    *   `BUS`
*   **Car (`COMPACT`)**: Can park in slots designated as:
    *   `COMPACT`
    *   `BUS`
    *   (Cannot park in `MOTORCYCLE` slots)
*   **Bus (`BUS`)**: Can park in slots designated as:
    *   `BUS` only
    *   (Cannot park in `MOTORCYCLE` or `COMPACT` slots)

### 3. Unparking Vehicles (`unparkVehicle(Vehicle v)`)

*   **Identify Level and Slots**: The method retrieves the `levelParked` and `slotsOccupied` from the vehicle object.
*   **Release Slots**:
    *   The `levelParked` for the vehicle is set to `null`.
    *   It iterates through the `slotsOccupied` by the vehicle:
        *   Each slot is added back to the list of available slots in its respective level.
        *   The `vehicleId` for each slot is set back to `null`, marking it as empty.
    *   The vehicle's `slotsOccupied` list is cleared.

### 3. Reporting Parking Status

*   **`printParkings()`**:
    *   This method provides an overview of the entire parking lot.
    *   It iterates through each `Level`.
    *   For each level, it prints the `levelNo` and then prints the IDs of all currently *available* (unoccupied) slots in that level.
*   **`printParking(Vehicle v)`**:
    *   This method provides the parking details for a specific vehicle.
    *   It prints the `vehicleId`, the `levelParked`, and the list of `Slot` objects (which includes their IDs) occupied by that vehicle.

## Project Capabilities

The Parking Lot Simulation system offers the following capabilities:

*   **Multi-Level Parking Structure**: The system can model parking lots with multiple levels, each configurable with a specific number of parking slots.
*   **Support for Diverse Vehicle Types**: It handles various vehicle types (Motorcycles, Cars, Buses), each with predefined slot requirements.
*   **Slot Type Designation**: Individual parking slots can be designated for specific vehicle types (e.g., MOTORCYCLE, COMPACT, BUS).
*   **Type-Based Parking Compatibility**: The system enforces rules about which vehicle types can park in which designated slot types (see "Slot Type Restrictions and Compatibility").
*   **Dynamic Slot Allocation**: The system dynamically allocates the required number of *contiguous* and *type-compatible* parking slots to a vehicle upon parking. It searches across levels to find suitable spots.
*   **Dynamic Slot Deallocation**: When a vehicle unparks, the system releases its occupied slots, making them available for other vehicles.
*   **Error Handling**: If a vehicle cannot be parked (e.g., no contiguous, compatible slots available), the system throws an exception.
*   **Parking Status Reporting**:
    *   **Overall Lot Status**: Provides a view of all levels and the IDs of currently available slots within them (`printParkings()`).
    *   **Specific Vehicle Status**: Reports the exact location (level and slot IDs) of a parked vehicle (`printParking(Vehicle v)`).
*   **Command-Line Interface**: The simulation is run via a command-line interface, with `Driver.java` providing an example of how to interact with the system.
*   **Extensible Design**: The use of an abstract `Vehicle` class and `VehicleType` enum allows for potential future expansion with new vehicle types.

## Observed Limitations/Simplifications

The current implementation of the parking lot system has several simplifications and limitations:

*   **First-Come, First-Served (FCFS) Parking Strategy**: The system allocates slots based on iterating through levels and then through sorted available slots within each level. It takes the first available contiguous block of type-compatible slots that fits the vehicle's needs. No advanced optimization (e.g., minimizing fragmentation, specific area preference beyond type) is implemented.
*   **Slot Type Assignment**: While levels can be initialized with specific slot types, the mechanism for defining these types (a list passed to the `Level` constructor) is basic. There's no dynamic way to change slot types after initialization or more complex patterns for type distribution beyond repeating the last type if the list is shorter than the number of slots.
*   **No Advanced Features**: The simulation still lacks common real-world parking features such as:
    *   Parking fees calculation.
    *   Time tracking for parked vehicles.
    *   Reservation capabilities.
    *   Handling of more granular slot attributes (e.g., handicapped, EV charging, premium).
*   **Error Handling**: Error handling primarily relies on throwing generic `Exception` messages. More specific exception types are not used.
*   **Command-Line Interface Only**: Interaction remains via the command-line. No GUI is available.
*   **Slot Finding Logic**: The slot finding logic in `ParkingLot.parkVehicle()` was significantly refactored to include type compatibility and improve robustness. The previously noted potential bug regarding `prev` slot initialization and assumption of the first slot in `occSlots` has been addressed by the new iteration and checking mechanism. However, complex scenarios or extremely large parking lots might still reveal edge cases.
*   **No Persistence**: Parking lot state is not persisted.
*   **Fixed Slot Sizes per Vehicle Type**: Vehicle slot requirements are hardcoded in the `Vehicle` class constructor (though `Driver.java` demonstrates a manual override for testing).

## Conclusion

In summary, this project provides a foundational command-line simulation of a multi-level parking lot system with type-specific slot restrictions. It successfully models core functionalities such as parking various vehicle types (Motorcycles, Cars, Buses) based on contiguous slot availability and type compatibility, unparking vehicles, and reporting on parking status. The introduction of slot type restrictions adds a layer of realism. While it includes several simplifications and has areas for potential improvement (like advanced error handling, diverse parking strategies, and a GUI), it serves as a clear demonstration of the logic and data structures for managing a parking facility with type constraints.
