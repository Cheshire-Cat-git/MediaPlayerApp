package com.example.mediaplayerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaplayerapp.data.AuthRepository

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun checkAuthStatus() {
        _isLoggedIn.value = authRepository.isLoggedIn()
    }

    fun logout() {
        authRepository.logout()
        _isLoggedIn.value = false
    }
}