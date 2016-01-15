package pro.anton.averin.networking.testrest;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pro.anton.averin.networking.testrest.views.activities.TestRestActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestRestActivityTest {

    @Rule
    public ActivityTestRule<TestRestActivity> mActivityRule = new ActivityTestRule<>(
            TestRestActivity.class);

    @Test
    public void addHeadersButtonShowsPopup() {
        onView(withId(R.id.add_query_button)).perform(click());
        onView(withText(R.string.add_query)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingPostShowsPostBody() {
        onView(withId(R.id.checkbox_post)).perform(click());
        onView(withText(R.string.post_body)).check(matches(isDisplayed()));
    }

}
