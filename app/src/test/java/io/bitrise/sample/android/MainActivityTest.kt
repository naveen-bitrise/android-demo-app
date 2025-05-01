package io.bitrise.sample.android

import android.content.Intent
import android.os.Build
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P]) // Use an API level that matches your minSdkVersion or higher
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    
    @Before
    fun setup() {
        // Launch the activity
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }
    
    @Test
    fun testActivityCreation() {
        // Verify that the activity is created successfully
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity { activity ->
            assertNotNull("Activity should not be null", activity)
        }
    }
    
    @Test
    fun testActivityResumption() {
        // Verify that the activity can be resumed
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onActivity { activity ->
            assertEquals("Activity should be in RESUMED state", 
                Lifecycle.State.RESUMED, activity.lifecycle.currentState)
        }
    }
    
    @Test
    fun testLoginButtonExists() {
        // Verify that the login button exists in the layout
        scenario.onActivity { activity ->
            val loginButton = activity.findViewById<Button>(R.id.btn_login)
            assertNotNull("Login button should exist in the layout", loginButton)
            assertEquals("Button text should be 'Login'", "Login", loginButton.text.toString())
        }
    }
    
    @Test
    fun testLoginButtonClick() {
        // Verify that clicking the login button launches the login activity
        scenario.onActivity { activity ->
            // Get the login button and click it
            val loginButton = activity.findViewById<Button>(R.id.btn_login)
            loginButton.performClick()
            
            // Get the shadow of the activity to verify the intent
            val shadowActivity = shadowOf(activity)
            val startedIntent = shadowActivity.nextStartedActivityForResult
            
            // Verify the intent
            assertNotNull("Intent should not be null", startedIntent)
            assertEquals("Request code should match", 1001, startedIntent.requestCode)
            
            val intentComponent = startedIntent.intent.component
            assertNotNull("Intent component should not be null", intentComponent)
            assertTrue("Intent should target LoginActivity",
                intentComponent!!.className.contains("LoginActivity"))
        }
    }
    
    @Test
    fun testActivityResult_WhenResultIsOk() {
        // Verify that the activity handles RESULT_OK correctly
        scenario.onActivity { activity ->
            // Create a mock result intent
            val resultIntent = Intent()
            
            // Simulate receiving activity result
            shadowOf(activity).receiveResult(
                Intent(activity, Class.forName("io.bitrise.sample.android.feature.login.ui.LoginActivity")),
                RESULT_OK,
                resultIntent
            )
            
            // We would verify some state change here based on your implementation
            // This might test a UI change, a method call, etc.
            // For example: verify(mockToast).show()
        }
    }
    
    @Test
    fun testActivityResult_WhenResultIsCanceled() {
        // Verify that the activity handles RESULT_CANCELED correctly
        scenario.onActivity { activity ->
            // Create a mock result intent
            val resultIntent = Intent()
            
            // Simulate receiving activity result
            shadowOf(activity).receiveResult(
                Intent(activity, Class.forName("io.bitrise.sample.android.feature.login.ui.LoginActivity")),
                RESULT_CANCELED,
                resultIntent
            )
            
            // We would verify some state change here based on your implementation
        }
    }
    
    @Test
    fun testLifecycleDestruction() {
        // Verify that the activity can be destroyed properly
        scenario.moveToState(Lifecycle.State.DESTROYED)
        // No assertions needed - we're just verifying it doesn't crash
    }
    
    companion object {
        // Constants that match your MainActivity
        private const val RESULT_OK = -1
        private const val RESULT_CANCELED = 0
    }
}