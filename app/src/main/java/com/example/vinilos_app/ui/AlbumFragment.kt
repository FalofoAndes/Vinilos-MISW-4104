package com.example.vinyls_jetpack_application.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.ui.adapter.AlbumAdapter
import com.example.vinyls_jetpack_application.viewmodels.AlbumCatalogViewModel

class AlbumFragment : Fragment(R.layout.album_fragment) {

    private lateinit var albumCatalogViewModel: AlbumCatalogViewModel
    private lateinit var adapter: AlbumAdapter

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
        adapter = AlbumAdapter(emptyList())
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
