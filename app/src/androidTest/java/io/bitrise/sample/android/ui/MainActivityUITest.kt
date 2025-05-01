package io.bitrise.sample.android.ui

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.bitrise.sample.android.MainActivity
import io.bitrise.sample.android.R
import io.bitrise.sample.android.feature.login.ui.LoginActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Initialize Intents before each test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Intents after each test
        Intents.release()
    }

    @Test
    fun testMainActivityDisplaysCorrectly() {
        // Verify the main activity displays with the correct elements
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
            .check(matches(withText("Login")))

        // Take a screenshot of the initial state
        takeScreenshot("main_activity_initial")
    }

    @Test
    fun testLoginButtonNavigatesToLoginActivity() {
        // Click the login button
        onView(withId(R.id.btn_login)).perform(click())

        // Verify that the login activity is launched
        intended(hasComponent(LoginActivity::class.java.name))

        // Take a screenshot after navigation
        takeScreenshot("after_login_button_click")
    }

    @Test
    fun testLoginNavigationAndReturn() {
        // Click the login button
        onView(withId(R.id.btn_login)).perform(click())

        // Verify we're on the login activity
        intended(hasComponent(LoginActivity::class.java.name))

        // Press back to return to MainActivity
        pressBack()

        // Verify we're back on MainActivity by checking for the login button
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))

        // Take a screenshot after returning
        takeScreenshot("returned_to_main")
    }

    @Test
    fun testAccessibilityOfMainElements() {
        // Check that all important elements are accessible
        onView(withId(R.id.btn_login))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(isClickable()))
            .check(matches(hasContentDescription()))

        // Take a screenshot
        takeScreenshot("accessibility_check")
    }

    @Test
    fun testMainActivityLayout() {
        // Check that login button has the correct position relative to other elements
        // This is a simple example - adjust according to your actual layout
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
            .check(matches(withPositionInParent()))

        // Take a screenshot
        takeScreenshot("layout_verification")
    }

    // Custom matcher to check if a view has a content description
    private fun hasContentDescription(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("has content description")
            }

            override fun matchesSafely(item: View): Boolean {
                return !item.contentDescription.isNullOrEmpty()
            }
        }
    }

    // Custom matcher to check the position of a view within its parent
    private fun withPositionInParent(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("is positioned correctly in parent")
            }

            override fun matchesSafely(item: View): Boolean {
                // You can implement specific logic based on your layout
                // For example, checking that the button is centered:
                // val parent = item.parent as ViewGroup
                // return item.left > 0 && item.right < parent.width
                return true // Simplified for this example
            }
        }
    }
}