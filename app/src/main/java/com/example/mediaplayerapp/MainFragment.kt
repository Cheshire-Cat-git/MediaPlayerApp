package com.example.mediaplayerapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mediaplayerapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var selectedType: String? = null

    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bundle = Bundle().apply {
                putString("uri", it.toString())
            }
            if (selectedType == "audio") {
                findNavController().navigate(R.id.audioPlayerFragment, bundle)
            } else if (selectedType == "video") {
                findNavController().navigate(R.id.videoPlayerFragment, bundle)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAudio.setOnClickListener {
            selectedType = "audio"
            getContentLauncher.launch("audio/*")
        }

        binding.btnVideo.setOnClickListener {
            selectedType = "video"
            getContentLauncher.launch("video/*")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedType", selectedType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedType = savedInstanceState?.getString("selectedType")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}