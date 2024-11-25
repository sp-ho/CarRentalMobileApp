package com.example.carrentalproject;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CarTest {

    private Car car;

    @Before
    public void setUp(){
        car = new Car("HD1", "Honda", "CR-V", 100.00, "1, avenue A");
    }

    @Test
    public void testGetCarId(){
        assertEquals("HD1", car.getCarId());
    }

    @Test
    public void testGetCarMake(){
        assertEquals("Honda", car.getCarMake());
    }

    @Test
    public void testGetCarModel(){
        assertEquals("CR-V", car.getCarModel());
    }

    @Test
    public void testGetCarRentalFee(){
        assertEquals(100.00, car.getCarRentalFee(), 0.01); // Check with a delta for floating-point comparison
    }

    @Test
    public void testGetCarAddress(){
        assertEquals("1, avenue A", car.getCarAddress());
    }
}
