package io.bitrise.sample.android.feature.login.domain

import io.bitrise.sample.android.feature.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(private val repository: LoginRepository = LoginRepository()) {
    
    suspend fun execute(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            repository.login(email, password)
        }
    }
}