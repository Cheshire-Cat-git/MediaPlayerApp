package com.example.mediaplayerapp

import com.example.mediaplayerapp.data.AuthRepositoryImpl
import com.example.mediaplayerapp.data.TokenManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class AuthRepositoryTest {

    private lateinit var tokenManager: TokenManager
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        tokenManager = mock(TokenManager::class.java)
        repository = AuthRepositoryImpl(tokenManager)
    }

    @Test
    fun isLoggedIn() {
        `when`(tokenManager.isTokenValid(any())).thenReturn(true)
        assertTrue(repository.isLoggedIn())

        `when`(tokenManager.isTokenValid(any())).thenReturn(false)
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun loginSuccess() {
        val result = repository.login("user", "password")
        assertTrue(result)
        verify(tokenManager).saveToken(any(), any())
    }

    @Test
    fun loginBlankUsername() {
        val result = repository.login("", "password")
        assertFalse(result)
    }

    @Test
    fun loginBlankPassword() {
        val result = repository.login("user", "")
        assertFalse(result)
    }

    @Test
    fun logout() {
        repository.logout()
        verify(tokenManager).clearToken()
    }
}