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
    private lateinit var adapter: AlbumDetailAdapter
    private var albumId: Int? = null
    private lateinit var binding: AlbumDetailItemBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recuperar el argumento del álbum
        val args: AlbumDetailFragmentArgs by navArgs()
        albumId = args.idAlbum // Almacenar el ID del álbum
        val repository = AlbumRepository(requireContext())
        albumDetailViewModel = ViewModelProvider(this, AlbumDetailViewModelFactory(repository))
            .get(AlbumDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.album_detail_fragment, container, false)
        binding = AlbumDetailItemBinding.inflate(inflater, container, false)

        // Recuperar el ID del álbum pasado como argumento
        albumId = arguments?.let { AlbumDetailFragmentArgs.fromBundle(it).idAlbum }

        // Observar los cambios en los detalles del álbum
        albumDetailViewModel.albumDetail.observe(viewLifecycleOwner, Observer { album ->
            if (album != null) {
                // Inicializar el adaptador
                adapter = AlbumDetailAdapter(album)
                val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_album_detail)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        })

        // Observar errores
        albumDetailViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Cargar los detalles del álbum
        albumId?.let { albumDetailViewModel.loadAlbumDetail(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el RecyclerView y su adaptador con una lista vacía inicialmente
        val adapter = TrackAdapter(emptyList())
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTracks.adapter = adapter

        // Observa los detalles del álbum en el ViewModel
        albumDetailViewModel.albumDetail.observe(viewLifecycleOwner) { album ->
            album?.let {
                binding.album = it // Asigna el álbum al binding
                adapter.updateTracks(it.tracks)
                adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
            }
        }

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