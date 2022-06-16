package net.rideshare_ptc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration tests for Ride Posting features in the app
 */
@RunWith(AndroidJUnit4.class)
public class RequestARideTests {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        login();
    }

    /**
     * Logs into the app.
     * Runs before all tests to ensure that they function properly.
     * Assume that tests in LoginActivityTests.java are all passing.
     */
    private void login() {
        //type in the username
        onView(withId(R.id.txtInputLoginEM)).perform(typeText("RSC14@students.ptcollege.edu"), ViewActions.closeSoftKeyboard());

        //type in the password
        onView(withId(R.id.txtInputLoginPW)).perform(typeText("Password12!"), ViewActions.closeSoftKeyboard());

        //click submit
        onView(withId(R.id.btnSubmitUser)).perform(click());
    }

    /**
     * Navigates to the Request A Ride page from the home page
     * Note: Will cause test/execution failure if this method is
     * not ran from the home page.
     */
    private void navigateToRequestARide(){
        onView(withId(R.id.btnMenuReq)).perform(click());
    }

    /**
     * Navigates back to the home page from the Request
     * A Ride page. Should be ran after tests that do not actually
     * perform operations on the Request A Ride Page.
     *
     * I'm using this just to be safe that
     * all tests function as intended.
     */
    private void leaveRequestARide(){
        onView(withId(R.id.btnReqRideRetMenu)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Test_RequestARide_AllElementsAreDisplayed(){
        navigateToRequestARide(); //navigate to request a ride from home page
        onView(withId(R.id.inptReqDateTime)).check(matches(isDisplayed()));
        onView(withId(R.id.inptReqPickUpLoc)).check(matches(isDisplayed()));
        onView(withId(R.id.inptReqDestLoc)).check(matches(isDisplayed()));
        onView(withId(R.id.ReqcheckBoxSmoking)).check(matches(isDisplayed()));
        onView(withId(R.id.ReqcheckBoxTalking)).check(matches(isDisplayed()));
        onView(withId(R.id.ReqcheckBoxHasCarseat)).check(matches(isDisplayed()));
        onView(withId(R.id.ReqcheckBoxEating)).check(matches(isDisplayed()));
        onView(withId(R.id.btnReqRideRetMenu)).check(matches(isDisplayed()));
        onView(withId(R.id.btnReqARide)).check(matches(isDisplayed()));
        leaveRequestARide();
    }

    /**
     * Posts a ride to the database using the app's Request a Ride
     * page. Then checks the database to ensure that the ride was posted
     * with the correct information.
     */
    @Test
    public void Test_RequestARide_PostInformationIsCorrect(){
        
    }
}