package com.example.mediaplayerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaplayerapp.data.AuthRepository

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun checkAuthStatus() {
        _isLoggedIn.value = authRepository.isLoggedIn()
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value = LoginResult.Error("Username and password cannot be empty")
            return
        }
        val success = authRepository.login(username, password)
        if (success) {
            _loginResult.value = LoginResult.Success
        } else {
            _loginResult.value = LoginResult.Error("Invalid credentials")
        }
    }
}