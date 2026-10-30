package com.example.mediaplayerapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.mediaplayerapp.databinding.FragmentVideoPlayerBinding

class VideoPlayerFragment : Fragment() {

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private var currentPosition: Int = 0
    private var isPlayingState: Boolean = true
    private var uriString: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null

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
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlay.text = if (isPlayingState) "Pause" else "Play"

        uriString?.let { uriStr ->
            val uri = Uri.parse(uriStr)
            binding.videoView.setVideoURI(uri)

            binding.videoView.setOnPreparedListener { mp ->
                val duration = mp.duration
                if (duration > 0) {
                    binding.seekBarVideo.max = duration
                }
                if (currentPosition > 0) {
                    binding.videoView.seekTo(currentPosition)
                }
                updateTimeText(currentPosition, duration)

                if (isPlayingState) {
                    binding.videoView.start()
                    binding.btnPlay.text = "Pause"
                    startProgressUpdate()
                } else {
                    binding.btnPlay.text = "Play"
                }
            }

            binding.videoView.setOnCompletionListener {
                isPlayingState = false
                binding.btnPlay.text = "Play"
                val duration = binding.videoView.duration
                binding.seekBarVideo.progress = duration
                updateTimeText(duration, duration)
                stopProgressUpdate()
            }
        }

        binding.btnPlay.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                isPlayingState = false
                currentPosition = binding.videoView.currentPosition
                binding.btnPlay.text = "Play"
                stopProgressUpdate()
            } else {
                binding.videoView.start()
                isPlayingState = true
                binding.btnPlay.text = "Pause"
                startProgressUpdate()
            }
        }

        binding.seekBarVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.videoView.seekTo(progress)
                    currentPosition = progress
                    updateTimeText(progress, binding.videoView.duration)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startProgressUpdate() {
        stopProgressUpdate()
        updateRunnable = object : Runnable {
            override fun run() {
                if (_binding != null && binding.videoView.isPlaying) {
                    val cur = binding.videoView.currentPosition
                    val dur = binding.videoView.duration
                    if (dur > 0) {
                        binding.seekBarVideo.max = dur
                    }
                    binding.seekBarVideo.progress = cur
                    updateTimeText(cur, dur)
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(updateRunnable!!)
    }

    private fun stopProgressUpdate() {
        updateRunnable?.let { handler.removeCallbacks(it) }
        updateRunnable = null
    }

    private fun updateTimeText(currentMs: Int, totalMs: Int) {
        val curSec = (currentMs.coerceAtLeast(0) / 1000) % 60
        val curMin = (currentMs.coerceAtLeast(0) / 1000) / 60
        val totSec = (totalMs.coerceAtLeast(0) / 1000) % 60
        val totMin = (totalMs.coerceAtLeast(0) / 1000) / 60
        binding.tvVideoTime.text = String.format("%02d:%02d / %02d:%02d", curMin, curSec, totMin, totSec)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val pos = if (_binding != null && binding.videoView.currentPosition > 0) {
            binding.videoView.currentPosition
        } else {
            currentPosition
        }
        val playing = if (_binding != null) binding.videoView.isPlaying || isPlayingState else isPlayingState
        outState.putInt(KEY_POSITION, pos)
        outState.putBoolean(KEY_IS_PLAYING, playing)
        outState.putString(KEY_URI, uriString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopProgressUpdate()
        if (_binding != null) {
            currentPosition = binding.videoView.currentPosition
            binding.videoView.stopPlayback()
        }
        _binding = null
    }

    companion object {
        private const val KEY_POSITION = "key_video_position"
        private const val KEY_IS_PLAYING = "key_video_is_playing"
        private const val KEY_URI = "key_video_uri"
    }
}