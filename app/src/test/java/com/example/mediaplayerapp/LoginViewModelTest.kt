package com.example.mediaplayerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mediaplayerapp.data.AuthRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        viewModel = LoginViewModel(authRepository)
    }

    @Test
    fun loginSuccess() {
        val username = "validUser"
        val password = "validPassword"
        `when`(authRepository.login(username, password)).thenReturn(true)

        viewModel.login(username, password)

        verify(authRepository).login(username, password)
        val result = viewModel.loginResult.value
        assertTrue(result is LoginResult.Success)
    }

    @Test
    fun loginFailure() {
        val username = "invalidUser"
        val password = "invalidPassword"
        `when`(authRepository.login(username, password)).thenReturn(false)

        viewModel.login(username, password)

        verify(authRepository).login(username, password)
        val result = viewModel.loginResult.value
        assertTrue(result is LoginResult.Error)
        assertEquals("Invalid credentials", (result as LoginResult.Error).message)
    }

    @Test
    fun loginBlankUsername() {
        val username = ""
        val password = "somePassword"

        viewModel.login(username, password)

        verify(authRepository, never()).login(username, password)
        val result = viewModel.loginResult.value
        assertTrue(result is LoginResult.Error)
        assertEquals("Username and password cannot be empty", (result as LoginResult.Error).message)
    }

    @Test
    fun loginBlankPassword() {
        val username = "someUser"
        val password = "   "

        viewModel.login(username, password)

        verify(authRepository, never()).login(username, password)
        val result = viewModel.loginResult.value
        assertTrue(result is LoginResult.Error)
        assertEquals("Username and password cannot be empty", (result as LoginResult.Error).message)
    }

    @Test
    fun checkAuthLoggedIn() {
        `when`(authRepository.isLoggedIn()).thenReturn(true)

        viewModel.checkAuthStatus()

        verify(authRepository).isLoggedIn()
        assertEquals(true, viewModel.isLoggedIn.value)
    }

    @Test
    fun checkAuthNotLoggedIn() {
        `when`(authRepository.isLoggedIn()).thenReturn(false)

        viewModel.checkAuthStatus()

        verify(authRepository).isLoggedIn()
        assertEquals(false, viewModel.isLoggedIn.value)
    }
}