package net.rideshare_ptc;


import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest //"You can also create a medium test but, hey go big or go home." -Cottrell
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void Test_LoginPage_EmailInputIsPresent()
    {
        onView(withHint("user@students.ptc.edu")).check(matches(isDisplayed()));
    }

    /**
     * Tests that the password input is present on the login
     * page
     */
    @Test
    public void Test_LoginPage_PasswordInputIsPresent()
    {
        onView(withHint("Password")).check(matches(isDisplayed()));
    }

    /**
     * Tests that, given correct credentials, the user
     * can login via the login page.
     *
     * REMEMBER TO HAVE THE WEBSERVICE RUNNING ON YOUR LOCALHOST WHEN YOU RUN THIS TEST
     */
    @Test
    public void Test_LoginPage_LogsInWithCredentials()
    {
        //type in the username
        onView(withId(R.id.txtInputLoginEM)).perform(typeText("RSC14@students.ptcollege.edu"), ViewActions.closeSoftKeyboard());

        //type in the password
        onView(withId(R.id.txtInputLoginPW)).perform(typeText("Password12!"), ViewActions.closeSoftKeyboard());

        //click submit
        onView(withId(R.id.btnSubmitUser)).perform(click());

        onView(withId(R.id.txtPostSucc)).check(matches(isDisplayed()));

    }

    /**
     * Tests that, given INCORRECT login information,
     * the user cannot login (is not redirected away from the login page)
     *
     * Assumes that the app does not redirect if the user inputs bad login information
     */
    @Test
    public void Test_LoginPage_DoesNotRedirectWithIncorrectCredentials()
    {
        //type in the username
        onView(withId(R.id.txtInputLoginEM)).perform(typeText("fakeUser@iAmEpic.com"), ViewActions.closeSoftKeyboard());

        //type in the password
        onView(withId(R.id.txtInputLoginPW)).perform(typeText("FakePasswordForMyEquallyFakeEmail!"), ViewActions.closeSoftKeyboard());

        //click submit
        onView(withId(R.id.btnSubmitUser)).perform(click());

        //Check to see that we are still on the login page
        onView(withId(R.id.txtLoginMessage)).check(matches(isDisplayed()));
    }

}