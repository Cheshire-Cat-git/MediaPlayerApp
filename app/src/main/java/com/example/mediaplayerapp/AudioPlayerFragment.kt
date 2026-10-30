package com.example.mediaplayerapp

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediaplayerapp.databinding.FragmentAudioPlayerBinding

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var isPlayingState: Boolean = true
    private var uriString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_POSITION, 0)
            isPlayingState = savedInstanceState.getBoolean(KEY_IS_PLAYING, true)
            uriString = savedInstanceState.getString(KEY_URI)
        } else {
            uriString = arguments?.getString("uri")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFilePath.text = uriString ?: ""

        uriString?.let { uriStr ->
            setupMediaPlayer(Uri.parse(uriStr))
        }

        binding.btnPlayPause.setOnClickListener {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    this@AudioPlayerFragment.currentPosition = player.currentPosition
                    isPlayingState = false
                    binding.btnPlayPause.text = "Play"
                } else {
                    player.start()
                    isPlayingState = true
                    binding.btnPlayPause.text = "Pause"
                }
            }
        }
    }

    private fun setupMediaPlayer(uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(requireContext(), uri)
                setOnPreparedListener { player ->
                    if (this@AudioPlayerFragment.currentPosition > 0) {
                        player.seekTo(this@AudioPlayerFragment.currentPosition)
                    }
                    if (isPlayingState) {
                        player.start()
                        binding.btnPlayPause.text = "Pause"
                    } else {
                        binding.btnPlayPause.text = "Play"
                    }
                }
                setOnCompletionListener {
                    isPlayingState = false
                    this@AudioPlayerFragment.currentPosition = 0
                    binding.btnPlayPause.text = "Play"
                }
                prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val pos = mediaPlayer?.currentPosition ?: currentPosition
        val playing = mediaPlayer?.isPlaying ?: isPlayingState
        outState.putInt(KEY_POSITION, pos)
        outState.putBoolean(KEY_IS_PLAYING, playing)
        outState.putString(KEY_URI, uriString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                currentPosition = player.currentPosition
                isPlayingState = true
            } else {
                currentPosition = player.currentPosition
                isPlayingState = false
            }
            player.release()
        }
        mediaPlayer = null
        _binding = null
    }

    companion object {
        private const val KEY_POSITION = "key_audio_position"
        private const val KEY_IS_PLAYING = "key_audio_is_playing"
        private const val KEY_URI = "key_audio_uri"
    }
}