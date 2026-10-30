package com.example.mediaplayerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mediaplayerapp.data.AuthRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        viewModel = MainViewModel(authRepository)
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

    @Test
    fun logout() {
        viewModel.logout()

        verify(authRepository).logout()
        assertEquals(false, viewModel.isLoggedIn.value)
    }
}