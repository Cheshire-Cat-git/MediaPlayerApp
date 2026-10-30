package com.example.mediaplayerapp.data

import java.util.UUID

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun login(username: String, password: String): Boolean
    fun logout()
}

class AuthRepositoryImpl(
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun isLoggedIn(): Boolean {
        return tokenManager.isTokenValid()
    }

    override fun login(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            return false
        }
        val token = "token_" + UUID.randomUUID().toString()
        tokenManager.saveToken(token)
        return true
    }

    override fun logout() {
        tokenManager.clearToken()
    }
}