package com.example.mediaplayerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mediaplayerapp.data.AuthRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class AudioPlayerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AudioPlayerViewModel

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        viewModel = AudioPlayerViewModel(authRepository)
    }

    @Test
    fun setFilePath() {
        val path = "content://media/external/audio/media/10"
        viewModel.setFilePath(path)
        assertEquals(path, viewModel.filePath.value)
    }

    @Test
    fun setFilePathNull() {
        viewModel.setFilePath(null)
        assertEquals("", viewModel.filePath.value)
    }

    @Test
    fun togglePlayPause() {
        assertTrue(viewModel.isPlaying.value == true)
        viewModel.togglePlayPause()
        assertFalse(viewModel.isPlaying.value == true)
        viewModel.togglePlayPause()
        assertTrue(viewModel.isPlaying.value == true)
    }

    @Test
    fun setPlaying() {
        viewModel.setPlaying(false)
        assertEquals(false, viewModel.isPlaying.value)

        viewModel.setPlaying(true)
        assertEquals(true, viewModel.isPlaying.value)
    }

    @Test
    fun updatePosition() {
        val position = 12500
        viewModel.updatePosition(position)
        assertEquals(position, viewModel.currentPosition.value)
    }

    @Test
    fun checkAuthStatus() {
        `when`(authRepository.isLoggedIn()).thenReturn(true)

        val result = viewModel.checkAuthStatus()

        verify(authRepository).isLoggedIn()
        assertTrue(result)
    }
}