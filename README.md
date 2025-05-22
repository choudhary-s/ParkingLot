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
    *   Properties: `id` (int), `vehicleId` (String - null if the slot is empty).
    *   Responsibilities: Holds information about its own ID and whether it's occupied by a vehicle.
*   **`Level`**: Represents a level within the parking lot.
    *   Properties: `levelNo` (int), `initialNoOfSlots` (int), `slots` (ArrayList<Slot>).
    *   Responsibilities: Manages a collection of slots on that particular level. Initializes a predefined number of slots upon creation.
*   **`ParkingLot`**: Represents the entire parking lot.
    *   Properties: `levels` (ArrayList<Level>).
    *   Responsibilities:
        *   Manages a collection of levels.
        *   `parkVehicle(Vehicle v)`: Finds contiguous available slots across levels, allocates them to the vehicle, and updates the vehicle's and slots' statuses. Throws an exception if no suitable slot is found.
        *   `unparkVehicle(Vehicle v)`: Releases the slots occupied by a vehicle and updates their status.
        *   `printParkings()`: Prints the current status of all levels and available slots.
        *   `printParking(Vehicle v)`: Prints the parking details (level and slots) for a specific vehicle.

## Key Functionalities

The parking lot system provides the following key functionalities:

### 1. Parking Vehicles (`parkVehicle(Vehicle v)`)

*   **Slot Requirement**: The method first determines the number of contiguous slots (`n`) needed by the vehicle (`v.numOfSlotsNeeded`).
*   **Iterating Through Levels**: It iterates through each `Level` in the `ParkingLot`.
*   **Searching for Contiguous Slots**: Within each level, it iterates through the available slots (`avlSlots`).
    *   It attempts to find `n` contiguous slots by checking if the ID of the current slot is sequential to the previous one.
    *   A temporary list (`occSlots`) stores potential candidate slots. If a non-contiguous slot is found, `occSlots` is cleared and the search restarts from the current slot.
*   **Allocation**:
    *   If `n` contiguous slots are found:
        *   These slots are removed from the level's list of available slots (`avlSlots`).
        *   The `vehicleId` of the parked vehicle is set for each of these occupied slots.
        *   The vehicle's `slotsOccupied` list is updated with these slots.
        *   The vehicle's `levelParked` is set to the current level number.
        *   The search stops, and the vehicle is considered parked.
*   **No Slot Found**: If the iteration completes without finding suitable contiguous slots in any level, an exception is thrown, indicating that the vehicle cannot be parked.

### 2. Unparking Vehicles (`unparkVehicle(Vehicle v)`)

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
*   **Support for Diverse Vehicle Types**: It handles various vehicle types, including:
    *   Motorcycles (`Bike` entity, typically 1 slot)
    *   Cars (`Car` entity, typically 2 slots)
    *   Buses (`Bus` entity, typically 4 slots)
    Each vehicle type has a predefined slot requirement.
*   **Dynamic Slot Allocation**: The system dynamically allocates the required number of *contiguous* parking slots to a vehicle upon parking. It searches across levels to find suitable spots.
*   **Dynamic Slot Deallocation**: When a vehicle unparks, the system releases its occupied slots, making them available for other vehicles.
*   **Basic Error Handling**: If a vehicle cannot be parked (e.g., no contiguous slots available that meet its requirements), the system throws an exception.
*   **Parking Status Reporting**:
    *   **Overall Lot Status**: Provides a view of all levels and the IDs of currently available slots within them (`printParkings()`).
    *   **Specific Vehicle Status**: Reports the exact location (level and slot IDs) of a parked vehicle (`printParking(Vehicle v)`).
*   **Command-Line Interface**: The simulation is run via a command-line interface, with `Driver.java` providing an example of how to interact with the system.
*   **Extensible Design**: The use of an abstract `Vehicle` class and `VehicleType` enum allows for potential future expansion with new vehicle types.

## Observed Limitations/Simplifications

The current implementation of the parking lot system has several simplifications and limitations:

*   **First-Come, First-Served (FCFS) Parking**: The system allocates slots based on a simple FCFS strategy for the levels and the slots within them. It takes the first available contiguous block of slots that fits the vehicle's needs without any optimization (e.g., trying to minimize fragmentation or assigning specific areas for certain vehicle types).
*   **No Advanced Features**: The simulation lacks common real-world parking features such as:
    *   Parking fees calculation.
    *   Time tracking for parked vehicles.
    *   Reservation capabilities.
    *   Handling of specific slot types (e.g., handicapped, EV charging).
*   **Basic Error Handling**: Error handling is rudimentary, primarily relying on throwing generic `Exception` messages (e.g., when no parking slot is found). More specific exception types or error codes are not used.
*   **Command-Line Interface Only**: The system can only be interacted with via a command-line interface. There is no Graphical User Interface (GUI).
*   **Potential Slot Finding Bug**: The `ParkingLot.parkVehicle()` method has a potential issue in its contiguous slot finding logic. If the very first slot of a level is part of a sequence that could fit a vehicle, but the sequence starts with `occSlots.add(avlSlots.get(index))` where `index` is 0, the `prev` slot might not be correctly initialized for the loop `if(curr.id==prev.id+1)`. This could lead to incorrect slot allocation or failure to find a valid spot in specific edge cases. For example, if a vehicle needs 2 slots and slots 1 and 2 are available, the current logic might only identify slot 1 if `prev` is not handled correctly before the loop begins checking `curr.id == prev.id + 1`. The initialization `Slot prev = avlSlots.get(index); occSlots.add(avlSlots.get(index));` followed by a loop starting `i=index+1` means `prev` is the first slot in a potential sequence, and `curr` is the second. If `n=1`, it works. If `n>1`, the first comparison `curr.id == prev.id + 1` is valid. However, the initial `occSlots.add(avlSlots.get(index))` assumes the first slot is always part of a potential match without prior validation, which might be an oversimplification.
*   **No Persistence**: Parking lot state (vehicle locations, available slots) is not persisted and is lost when the application ends.
*   **Fixed Slot Sizes per Vehicle Type**: The number of slots needed per vehicle type is hardcoded in the `Vehicle` class constructor.

## Conclusion

In summary, this project provides a foundational command-line simulation of a multi-level parking lot system. It successfully models core functionalities such as parking various vehicle types (Motorcycles, Cars, Buses) based on contiguous slot availability, unparking vehicles, and reporting on parking status. While it includes several simplifications and has areas for potential improvement and feature expansion (like advanced error handling, diverse parking strategies, and a GUI), it serves as a clear demonstration of the basic logic and data structures involved in managing a parking facility.
