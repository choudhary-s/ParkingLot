import entity.*;

import java.util.Arrays;

public class Driver {
    public static void main(String[] args) {
        Level l1 = new Level(1, 3);
        Level l2 = new Level(2, 4);
        ParkingLot pl = new ParkingLot(Arrays.asList(l1,l2));

        Car car1 = new Car("Car1");
        Car car2 = new Car("Car2");
        Car car3 = new Car("Car3");
        Car car4 = new Car("Car4");

        Bike bike1 = new Bike("Bike1");
        Bike bike2 = new Bike("Bike2");
        Bike bike3 = new Bike("Bike3");
        Bike bike4 = new Bike("Bike4");

        Bus bus1 = new Bus("Bus1");
        Bus bus2 = new Bus("Bus2");
        Bus bus3 = new Bus("Bus3");
        Bus bus4 = new Bus("Bus4");

        try {
            pl.parkVehicle(bike1);
            pl.printParkings();
            pl.printParking(bike1);

            pl.parkVehicle(bike2);
            pl.printParkings();
            pl.printParking(bike2);

            pl.parkVehicle(car1);
            pl.printParkings();
            pl.printParking(car1);

            pl.parkVehicle(car2);
            pl.printParkings();
            pl.printParking(car2);

            pl.unparkVehicle(car1);
            pl.printParkings();

            pl.unparkVehicle(car2);
            pl.printParkings();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
