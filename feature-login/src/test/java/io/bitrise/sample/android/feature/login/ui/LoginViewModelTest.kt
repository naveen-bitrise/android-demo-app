package io.bitrise.sample.android.feature.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.bitrise.sample.android.feature.login.domain.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Rule to make LiveData work on the main thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()
    
    @Mock
    private lateinit var loginUseCase: LoginUseCase
    
    private lateinit var viewModel: LoginViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Inject the mock into the ViewModel
        viewModel = LoginViewModel(loginUseCase)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `when email is empty, login should fail`() = runTest {
        // Act
        viewModel.login("", "password123")
        
        // Assert
        assertFalse(viewModel.loginResult.value ?: true)
    }
    
    @Test
    fun `when password is empty, login should fail`() = runTest {
        // Act
        viewModel.login("test@example.com", "")
        
        // Assert
        assertFalse(viewModel.loginResult.value ?: true)
    }
    
    @Test
    fun `when credentials are valid, login should succeed`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(loginUseCase.execute(email, password)).thenReturn(true)
        
        // Act
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle() // Process all coroutines
        
        // Assert
        assertTrue(viewModel.loginResult.value ?: false)
    }
    
    @Test
    fun `when login fails, loginResult should be false`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(loginUseCase.execute(email, password)).thenReturn(false)
        
        // Act
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle() // Process all coroutines
        
        // Assert
        assertFalse(viewModel.loginResult.value ?: true)
    }
    
    @Test
    fun `when exception occurs during login, loginResult should be false`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(loginUseCase.execute(email, password)).thenThrow(RuntimeException("Network error"))
        
        // Act
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle() // Process all coroutines
        
        // Assert
        assertFalse(viewModel.loginResult.value ?: true)
    }
}