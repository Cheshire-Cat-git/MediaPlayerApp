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

class VideoPlayerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: VideoPlayerViewModel

    @Before
    fun setUp() {
        authRepository = mock(AuthRepository::class.java)
        viewModel = VideoPlayerViewModel(authRepository)
    }

    @Test
    fun setUriString() {
        val uriStr = "content://media/external/video/media/42"
        viewModel.setUriString(uriStr)
        assertEquals(uriStr, viewModel.uriString.value)
    }

    @Test
    fun updatePositionAndDuration() {
        viewModel.setDuration(125000) // 02:05
        viewModel.updatePosition(65000)  // 01:05

        assertEquals(65000, viewModel.currentPosition.value)
        assertEquals(125000, viewModel.duration.value)
        assertEquals("01:05 / 02:05", viewModel.timerText.value)
    }

    @Test
    fun togglePlayPause() {
        assertTrue(viewModel.isPlaying.value == true)
        viewModel.togglePlayPause()
        assertFalse(viewModel.isPlaying.value == true)
    }

    @Test
    fun setPlaying() {
        viewModel.setPlaying(false)
        assertEquals(false, viewModel.isPlaying.value)

        viewModel.setPlaying(true)
        assertEquals(true, viewModel.isPlaying.value)
    }

    @Test
    fun formatTime() {
        val formatted = viewModel.formatTime(75000, 180000)
        assertEquals("01:15 / 03:00", formatted)
    }

    @Test
    fun checkAuthStatus() {
        `when`(authRepository.isLoggedIn()).thenReturn(true)

        val result = viewModel.checkAuthStatus()

        verify(authRepository).isLoggedIn()
        assertTrue(result)
    }
}