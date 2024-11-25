package com.example.carrentalproject;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import android.widget.Button;

public class MainFragmentUnitTest {
    private MainFragment mainFragment;

    @Before
    public void setUp() {
        mainFragment = new MainFragment();
        // Initialize the carList with mock data
        mainFragment.carList = new ArrayList<>(Arrays.asList(
                new Car("HD1", "Honda", "CR-V", 100.00, "1, avenue A"),
                new Car("HD2", "Honda", "Civic", 90.00, "1, avenue A"),
                new Car("HD3", "Honda", "Accord", 95.00, "2, avenue B")
        ));
    }

    @Test
    public void testCarListInitialization() {
        ArrayList<Car> carList = mainFragment.getCarList();
        assertNotNull(carList);
        assertEquals(3, carList.size()); // Adjust the size based on your mock data
    }

    @Test
    public void testCarListAdditionAndRemoval() {
        // Test adding a car to the list
        Car newCar = new Car("HD4", "Toyota", "Camry", 110.00, "3, avenue C");
        mainFragment.getCarList().add(newCar);
        ArrayList<Car> carList = mainFragment.getCarList();
        assertEquals(4, carList.size()); // Check if the size increased

        // Test removing a car from the list
        mainFragment.getCarList().remove(0); // Remove the first car
        carList = mainFragment.getCarList();
        assertEquals(3, carList.size()); // Check if the size decreased
        assertEquals("HD2", carList.get(0).getCarId()); // Check if the correct car was removed
    }

}
