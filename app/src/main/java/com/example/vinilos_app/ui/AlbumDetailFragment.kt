package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.ui.adapter.AlbumDetailAdapter
import com.example.vinilos_app.ui.adapter.TrackAdapter
import com.example.vinilos_app.viewmodels.AlbumDetailViewModel
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.AlbumDetailItemBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumDetailFragment : Fragment() {

    private lateinit var albumDetailViewModel: AlbumDetailViewModel
    private lateinit var trackAdapter: TrackAdapter
    private var albumId: Int? = null
    private lateinit var binding: AlbumDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: AlbumDetailFragmentArgs by navArgs()
        albumId = args.idAlbum
        val repository = AlbumRepository(requireContext())
        albumDetailViewModel = ViewModelProvider(this, AlbumDetailViewModelFactory(repository))
            .get(AlbumDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlbumDetailItemBinding.inflate(inflater, container, false)
        trackAdapter = TrackAdapter(emptyList())

        // Configura el RecyclerView para las pistas (tracks)
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTracks.adapter = trackAdapter

        // Observa el detalle del álbum
        albumDetailViewModel.albumDetail.observe(viewLifecycleOwner, Observer { album ->
            album?.let {
                binding.album = it
                trackAdapter.updateTracks(it.tracks) // Actualiza el adaptador con los tracks
                trackAdapter.notifyDataSetChanged() // Notifica al adaptador
            }
        })

        // Observa errores
        albumDetailViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Cargar los detalles del álbum
        albumId?.let { albumDetailViewModel.loadAlbumDetail(it) }

        return binding.root
    }
}

class AlbumDetailViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}