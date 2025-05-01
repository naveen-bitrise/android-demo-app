package io.bitrise.sample.android.feature.login.domain

import io.bitrise.sample.android.feature.login.data.LoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginUseCaseTest {
    
    @Mock
    private lateinit var repository: LoginRepository
    
    private lateinit var useCase: LoginUseCase
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = LoginUseCase(repository)
    }
    
    @Test
    fun `execute should return true when repository returns true`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(repository.login(email, password)).thenReturn(true)
        
        // Act
        val result = useCase.execute(email, password)
        
        // Assert
        assertTrue(result)
    }
    
    @Test
    fun `execute should return false when repository returns false`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(repository.login(email, password)).thenReturn(false)
        
        // Act
        val result = useCase.execute(email, password)
        
        // Assert
        assertFalse(result)
    }
}