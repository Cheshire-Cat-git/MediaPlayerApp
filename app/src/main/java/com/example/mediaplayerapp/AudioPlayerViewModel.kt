package com.example.mediaplayerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaplayerapp.data.AuthRepository

class AudioPlayerViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _filePath = MutableLiveData<String>()
    val filePath: LiveData<String> = _filePath

    private val _isPlaying = MutableLiveData<Boolean>(true)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData<Int>(0)
    val currentPosition: LiveData<Int> = _currentPosition

    fun setFilePath(path: String?) {
        _filePath.value = path ?: ""
    }

    fun togglePlayPause() {
        _isPlaying.value = !(_isPlaying.value ?: true)
    }

    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    fun updatePosition(pos: Int) {
        _currentPosition.value = pos
    }

    fun checkAuthStatus(): Boolean {
        return authRepository.isLoggedIn()
    }
}