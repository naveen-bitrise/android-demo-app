package io.bitrise.sample.android.feature.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bitrise.sample.android.feature.login.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase = LoginUseCase()) : ViewModel() {
    
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    
    fun login(email: String, password: String) {
        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = false
            return
        }
        
        viewModelScope.launch {
            try {
                val success = loginUseCase.execute(email, password)
                _loginResult.value = success
            } catch (e: Exception) {
                _loginResult.value = false
            }
        }
    }
}