package io.bitrise.sample.android.feature.login.data

import kotlinx.coroutines.delay

class LoginRepository {
    
    // In a real app, this would call an API service
    suspend fun login(email: String, password: String): Boolean {
        // Simulate network delay
        delay(1000)
        
        // For demo purposes, just check if email contains "@" and password length >= 6
        return email.contains("@") && password.length >= 6
    }
}