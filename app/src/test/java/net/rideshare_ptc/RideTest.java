package net.rideshare_ptc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RideTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setRideID() {
        Ride testRide = new Ride();
        int expected = 120938123;
        testRide.setRideID(expected);
        int actual = testRide.getRideID();

        assertEquals(expected, actual);
    }

    @Test
    public void setPickUpLoc() {
        Ride testRide = new Ride();
        String expected = "Starting Location";
        testRide.setPickUpLoc(expected);
        String actual = testRide.getPickUpLoc();

        assertTrue("PickUpLoc setter did not properly set pickUpLoc\n" +
                "Expected: " + expected + "\nActual: " + actual, expected.equals(actual));
    }

    @Test
    public void setDest() {
        Ride testRide = new Ride();
        String expected = "Destination";
        testRide.setDest(expected);
        String actual = testRide.getDest();

        assertTrue("Destination setter did not properly set destination\n" +
                "Expected: " + expected + "\nActual: " + actual, expected.equals(actual));
    }

    @Test
    public void setDuration() {
        Ride testRide = new Ride();
        float expected = 15.0f;
        testRide.setDuration(expected);
        float actual = testRide.getDuration();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setDistance() {
        Ride testRide = new Ride();
        float expected = 15.0f;
        testRide.setDistance(expected);
        float actual = testRide.getDistance();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setCost() {
        Ride testRide = new Ride();
        float expected = 15.0f;
        testRide.setCost(expected);
        float actual = testRide.getCost();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setDriverID() {
        Ride testRide = new Ride();
        String expected = "120398120984512";
        testRide.setDriverID(expected);
        String actual = testRide.getDriverID();

        assertTrue("DriverID setter did not properly set DriverID\n" +
                "Expected: " + expected + "\nActual: " + actual, expected.equals(actual));
    }

    @Test
    public void setRiderID() {
        Ride testRide = new Ride();
        String expected = "120398120984512";
        testRide.setRiderID(expected);
        String actual = testRide.getRiderID();

        assertTrue("RiderID setter did not properly set RiderID\n" +
                "Expected: " + expected + "\nActual: " + actual, expected.equals(actual));
    }

    @Test
    public void setDriverScore() {
        Ride testRide = new Ride();
        float expected = 10.0f;
        testRide.setDriverScore(expected);
        float actual = testRide.getDriverScore();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setRiderScore() {
        Ride testRide = new Ride();
        float expected = 10.0f;
        testRide.setRiderScore(expected);
        float actual = testRide.getRiderScore();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setRideDate() {
        Ride testRide = new Ride();
        String expected = "12-12-2000";
        testRide.setRideDate(expected);
        String actual = testRide.getRideDate();

        assertTrue("RiderID setter did not properly set RiderID\n" +
                "Expected: " + expected + "\nActual: " + actual, expected.equals(actual));
    }

    @Test
    public void setSmoking() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setSmoking(expected);
        Byte actual = testRide.getSmoking();

        assertEquals(expected, actual);
    }

    @Test
    public void setEating() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setEating(expected);
        Byte actual = testRide.getEating();

        assertEquals(expected, actual);
    }

    @Test
    public void setTalking() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setTalking(expected);
        Byte actual = testRide.getTalking();

        assertEquals(expected, actual);
    }

    @Test
    public void setCarseat() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setCarseat(expected);
        Byte actual = testRide.getCarseat();

        assertEquals(expected, actual);
    }

    @Test
    public void setIsTaken() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setIsTaken(expected);
        Byte actual = testRide.getIsTaken();

        assertEquals(expected, actual);
    }

    @Test
    public void setIsCompleted() {
        Ride testRide = new Ride();
        Byte expected = 1;
        testRide.setIsCompleted(expected);
        Byte actual = testRide.getIsCompleted();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Byte oneBit = 1;
        Byte zeroBit = 0;
        Ride testRide = new Ride("Starting Loc", "Dest", "1234", "12-12-2000", zeroBit,  zeroBit, oneBit, oneBit);

        String expected = "Ride Details: \nPick Up: Starting Loc\nDestination: Dest\nDate and Time: 12-12-2000";
        String actual = testRide.toString();

        assertTrue("ToString did not properly format the ride information:\n" +
                "Expected: \n" + expected + "\n\nActual: \n" + actual, expected.equals(actual));

    }
}