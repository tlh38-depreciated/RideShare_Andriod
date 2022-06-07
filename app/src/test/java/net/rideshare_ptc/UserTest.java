package net.rideshare_ptc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Semi-Trivial Tests for the User class
 * These mainly test that the User setters and constructors work properly
 */
public class UserTest {

    //private static User testUser;
    private static final String expectedEmail = "thisEmail@students.ptcollege.edu";


    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }

    /**
     * Tests our constructor for User class
     * (only initializes email)
     */
    @Test
    public void Test_UserConstructor()
    {
        User testUser = new User(expectedEmail);
        String actual = testUser.getUserEmail();

        boolean areMatching = expectedEmail.equals(actual);
        assertTrue("Constructor did not set email properly\n" +
                "Expected: " + expectedEmail + "\nActual: " + actual, areMatching);
    }



    @Test
    public void Test_setUserEmail() {
        User testUser = new User("email!");
        testUser.setUserEmail(expectedEmail);
        String actual = testUser.getUserEmail();

        boolean areMatching = expectedEmail.equals(actual);
        assertTrue("Email Setter did not properly set email\n" +
                "Expected: " + expectedEmail + "\nActual: " + actual, areMatching);
    }

    @Test
    public void Test_setUserName() {
        User testUser = new User("email!");
        String expected = "User Name";
        testUser.setUserName(expected);
        String actual = testUser.getUserName();

        boolean areMatching = expected.equals(actual);
        assertTrue("UserName Setter did not properly set UserName\n" +
                "Expected: " + expected + "\nActual: " + actual, areMatching);

    }

    @Test
    public void Test_setUserID() {
        User testUser = new User("email!");
        String expected = "109287341029472039842";
        testUser.setUserID(expected);
        String actual = testUser.getUserID();

        boolean areMatching = expected.equals(actual);
        assertTrue("UserID Setter did not properly set UserID\n" +
                "Expected: " + expected + "\nActual: " + actual, areMatching);
    }

    @Test
    public void Test_setUserFName() {
        User testUser = new User("email!");
        String expected = "Joe";
        testUser.setUserFName(expected);
        String actual = testUser.getUserFName();

        boolean areMatching = expected.equals(actual);
        assertTrue("FName setter did not properly set FName\n" +
                "Expected: " + expected + "\nActual: " + actual, areMatching);
    }

    @Test
    public void Test_setUserLName() {
        User testUser = new User("email!");
        String expected = "Schmoe";
        testUser.setUserLName(expected);
        String actual = testUser.getUserLName();

        boolean areMatching = expected.equals(actual);
        assertTrue("LName setter did not properly set LName\n" +
                "Expected: " + expected + "\nActual: " + actual, areMatching);
    }

    @Test
    public void Test_setuDriverScore() {
        User testUser = new User("email!");
        float expected = 10.00f;
        testUser.setuDriverScore(expected);
        float actual = testUser.getuDriverScore();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void Test_setuRiderScore() {
        User testUser = new User("email!");
        float expected = 10.00f;
        testUser.setuRiderScore(expected);
        float actual = testUser.getuRiderScore();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void Test_setIsDriver() {
        User testUser = new User("email!");
        byte expected = 1;
        testUser.setIsDriver(expected);
        byte actual = testUser.getIsDriver();

        assertEquals(expected, actual);
    }

    @Test
    public void Test_setuStudID() {
        User testUser = new User("email!");
        Integer expected = 1203912;
        testUser.setuStudID(expected);
        Integer actual = testUser.getuStudID();

        assertEquals(expected, actual);
    }
}