package com.example.vinilos_app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.repository.TrackRepository
import com.example.vinilos_app.ui.adapter.TrackAdapter
import com.example.vinilos_app.viewmodels.AlbumDetailViewModel
import com.example.vinilos_app.viewmodels.SharedAlbumViewModel
import com.example.vinyls_jetpack_application.databinding.AlbumDetailItemBinding

class AlbumDetailFragment : Fragment() {

    private lateinit var albumDetailViewModel: AlbumDetailViewModel
    private lateinit var sharedAlbumViewModel: SharedAlbumViewModel
    private lateinit var trackAdapter: TrackAdapter
    private var albumId: Int? = null
    private lateinit var binding: AlbumDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: AlbumDetailFragmentArgs by navArgs()
        albumId = args.idAlbum

        val albumRepository = AlbumRepository(requireContext())
        val trackRepository = TrackRepository(requireContext())
        albumDetailViewModel = ViewModelProvider(
            this,
            AlbumDetailViewModelFactory(albumRepository, trackRepository)
        )[AlbumDetailViewModel::class.java]

        // Inicializa el ViewModel compartido
        sharedAlbumViewModel = ViewModelProvider(requireActivity())[SharedAlbumViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlbumDetailItemBinding.inflate(inflater, container, false)

        trackAdapter = TrackAdapter(emptyList())
        binding.recyclerViewTracks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        albumDetailViewModel.albumDetail.observe(viewLifecycleOwner) { album ->
            album?.let {
                Log.d("AlbumDetailFragment", "Album updated: $album") // Log para verificar actualizaciones
                binding.album = it
                trackAdapter.updateTracks(it.tracks)
            }
        }

        sharedAlbumViewModel.trackAdded.observe(viewLifecycleOwner) { newTrack ->
            newTrack?.let {
                val currentAlbum = albumDetailViewModel.albumDetail.value
                currentAlbum?.let { album ->
                    val updatedAlbum = album.copy(tracks = album.tracks + newTrack)
                    Log.d("AlbumDetailFragment", "Updating album with new track: $newTrack")
                    albumDetailViewModel.updateAlbum(updatedAlbum)
                }
                sharedAlbumViewModel.clearTrackAdded()
            }
        }

        albumDetailViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        binding.addTrackButton.setOnClickListener {
            albumId?.let { id ->
                val action = AlbumDetailFragmentDirections.actionAlbumDetailFragmentToAddTrackFragment(id)
                findNavController().navigate(action)
            }
        }

        albumId?.let { albumDetailViewModel.loadAlbumDetail(it) }

        return binding.root
    }
}

class AlbumDetailViewModelFactory(
    private val albumRepository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumDetailViewModel(albumRepository, trackRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
