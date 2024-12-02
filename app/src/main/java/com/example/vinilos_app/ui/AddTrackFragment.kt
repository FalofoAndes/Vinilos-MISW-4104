package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vinilos_app.models.Track
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.repository.TrackRepository
import com.example.vinilos_app.viewmodels.AlbumDetailViewModel
import com.example.vinilos_app.viewmodels.SharedAlbumViewModel
import com.example.vinyls_jetpack_application.databinding.AddTrackFragmentBinding

class AddTrackFragment : Fragment() {

    private lateinit var binding: AddTrackFragmentBinding
    private lateinit var albumDetailViewModel: AlbumDetailViewModel
    private lateinit var sharedAlbumViewModel: SharedAlbumViewModel
    private val args: AddTrackFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val albumRepository = AlbumRepository(requireContext())
        val trackRepository = TrackRepository(requireContext())
        val factory = AlbumDetailViewModelFactory(albumRepository, trackRepository)
        albumDetailViewModel = ViewModelProvider(this, factory)[AlbumDetailViewModel::class.java]

        // Inicializa el ViewModel compartido
        sharedAlbumViewModel = ViewModelProvider(requireActivity())[SharedAlbumViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTrackFragmentBinding.inflate(inflater, container, false)

        binding.saveTrack.setOnClickListener {
            val trackName = binding.trackName.text.toString()
            val trackDuration = binding.trackDuration.text.toString()

            if (trackName.isBlank() || trackDuration.isBlank()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTrack = Track(id = 0, name = trackName, duration = trackDuration)
            albumDetailViewModel.addTrackToAlbum(args.albumId, newTrack)

            // Notifica al ViewModel compartido
            sharedAlbumViewModel.notifyTrackAdded(newTrack)

            Toast.makeText(requireContext(), "Track agregado exitosamente", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.cancelTrack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}
