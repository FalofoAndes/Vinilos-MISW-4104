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
import com.example.vinilos_app.ui.adapter.AlbumAdapter
import com.example.vinilos_app.ui.adapter.AlbumDetailAdapter
import com.example.vinilos_app.viewmodels.AlbumCatalogViewModel
import com.example.vinilos_app.viewmodels.AlbumDetailViewModel
import com.example.vinyls_jetpack_application.R

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumDetailFragment : Fragment() {

    private var album: Album? = null // Este es tu modelo `Album`, debes obtener o crear la instancia de alguna forma
    private lateinit var albumDetailViewModel: AlbumDetailViewModel
    private lateinit var adapter: AlbumDetailAdapter
    private var albumId: Int? = null

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
        val view = inflater.inflate(R.layout.fragment_album_detail, container, false)

        // Recuperar el ID del álbum pasado como argumento
        albumId = arguments?.let { AlbumDetailFragmentArgs.fromBundle(it).idAlbum }

        // Observar los cambios en los detalles del álbum
        albumDetailViewModel.albumDetail.observe(viewLifecycleOwner, Observer { album ->
            if (album != null) {
                // Inicializar el adaptador
                adapter = AlbumDetailAdapter(album)
                // Suponiendo que tienes un RecyclerView o algún contenedor para mostrar el álbum
                val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_album_detail)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        })

        // Observar errores
        albumDetailViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            // Aquí puedes mostrar un mensaje de error al usuario, por ejemplo con un Toast o un Snackbar
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Cargar los detalles del álbum
        albumId?.let { albumDetailViewModel.loadAlbumDetail(it) }

        return view
    }
}



// Clase de fábrica para crear AlbumCatalogViewModel con argumentos
class AlbumDetailViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}