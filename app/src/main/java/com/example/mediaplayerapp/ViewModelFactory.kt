package com.example.mediaplayerapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mediaplayerapp.data.AuthRepository
import com.example.mediaplayerapp.data.AuthRepositoryImpl
import com.example.mediaplayerapp.data.TokenManager

class ViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(AudioPlayerViewModel::class.java) -> {
                AudioPlayerViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(VideoPlayerViewModel::class.java) -> {
                VideoPlayerViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            val tokenManager = TokenManager(context.applicationContext)
            val authRepository = AuthRepositoryImpl(tokenManager)
            return ViewModelFactory(authRepository)
        }
    }
}