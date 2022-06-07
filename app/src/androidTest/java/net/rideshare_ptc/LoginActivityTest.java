package net.rideshare_ptc;


import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;


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

    @Test
    public void Test_LoginPage_PasswordInputIsPresent()
    {
        onView(withHint("Password")).check(matches(isDisplayed()));
    }
}