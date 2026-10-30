package com.example.mediaplayerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaplayerapp.data.AuthRepository
import java.util.Locale

class VideoPlayerViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uriString = MutableLiveData<String?>()
    val uriString: LiveData<String?> = _uriString

    private val _isPlaying = MutableLiveData<Boolean>(true)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData<Int>(0)
    val currentPosition: LiveData<Int> = _currentPosition

    private val _duration = MutableLiveData<Int>(0)
    val duration: LiveData<Int> = _duration

    private val _timerText = MutableLiveData<String>("00:00 / 00:00")
    val timerText: LiveData<String> = _timerText

    fun setUriString(uri: String?) {
        _uriString.value = uri
    }

    fun setDuration(dur: Int) {
        _duration.value = dur
        updateTimerText()
    }

    fun updatePosition(pos: Int) {
        _currentPosition.value = pos
        updateTimerText()
    }

    fun togglePlayPause() {
        _isPlaying.value = !(_isPlaying.value ?: true)
    }

    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    private fun updateTimerText() {
        val cur = _currentPosition.value ?: 0
        val dur = _duration.value ?: 0
        _timerText.value = formatTime(cur, dur)
    }

    fun formatTime(currentMs: Int, totalMs: Int): String {
        val curSec = (currentMs.coerceAtLeast(0) / 1000) % 60
        val curMin = (currentMs.coerceAtLeast(0) / 1000) / 60
        val totSec = (totalMs.coerceAtLeast(0) / 1000) % 60
        val totMin = (totalMs.coerceAtLeast(0) / 1000) / 60
        return String.format(Locale.getDefault(), "%02d:%02d / %02d:%02d", curMin, curSec, totMin, totSec)
    }

    fun checkAuthStatus(): Boolean {
        return authRepository.isLoggedIn()
    }
}