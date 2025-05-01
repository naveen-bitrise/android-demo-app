package io.bitrise.sample.android.feature.login.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.bitrise.sample.android.feature.login.R
import io.bitrise.sample.android.feature.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewModel.loginResult.observe(this) { success ->
            if (success) {
                // Navigate to next screen or finish with result
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Login failed! Check credentials.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }
        
        binding.tvForgotPassword.setOnClickListener {
            // Handle forgot password
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnRegister.setOnClickListener {
            // Navigate to registration
            Toast.makeText(this, "Register clicked", Toast.LENGTH_SHORT).show()
        }
    }
}