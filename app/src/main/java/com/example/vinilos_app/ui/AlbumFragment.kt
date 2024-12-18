package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Album
import com.example.vinyls_jetpack_application.R
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.ui.adapter.AlbumAdapter
import com.example.vinilos_app.viewmodels.AlbumCatalogViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlbumFragment : Fragment(R.layout.album_fragment) {

    private lateinit var albumCatalogViewModel: AlbumCatalogViewModel
    private lateinit var adapter: AlbumAdapter
    private val albumList = listOf<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia de AlbumRepository
        val repository = AlbumRepository(requireContext())

        // Usar ViewModelProvider para inicializar AlbumCatalogViewModel con argumentos
        albumCatalogViewModel = ViewModelProvider(this, AlbumCatalogViewModelFactory(repository))
            .get(AlbumCatalogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_albums)
        adapter = AlbumAdapter(albumList) { albumId  ->
            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(albumId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Update UI with album list
        albumCatalogViewModel.albumCatalog.observe(viewLifecycleOwner) { albumList ->
            adapter.updateAlbums(albumList)
        }

        // Observe errors and show toast
        albumCatalogViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Load album catalog
        albumCatalogViewModel.loadAlbumCatalog()

        val addAlbumButton = view.findViewById<FloatingActionButton>(R.id.fab_add_album)

        addAlbumButton.setOnClickListener {
            // Navegar al fragment de agregar álbum
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AddAlbumFragment())
                .addToBackStack(null) //
                .commit()
        }

    }
}

// Clase de fábrica para crear AlbumCatalogViewModel con argumentos
class AlbumCatalogViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumCatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumCatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
