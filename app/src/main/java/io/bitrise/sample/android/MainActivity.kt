package io.bitrise.sample.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import io.bitrise.sample.android.feature.login.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    
    // Register for activity result using the new API
    private val loginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // User logged in successfully
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            // You might want to update UI or navigate to another screen here
        } else {
            // User cancelled login or it failed
            Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Find login button in your layout
        val loginButton = findViewById<Button>(R.id.btn_login)
        
        // Set click listener to navigate to LoginActivity using the new approach
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            loginLauncher.launch(intent)
        }
    }
}