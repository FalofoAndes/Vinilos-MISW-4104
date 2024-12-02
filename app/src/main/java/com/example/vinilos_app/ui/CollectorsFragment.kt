package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos_app.models.Collector
import com.example.vinyls_jetpack_application.R
import com.example.vinilos_app.repository.CollectorsRepository
import com.example.vinilos_app.ui.adapter.AlbumAdapter
import com.example.vinilos_app.ui.adapter.CollectorsAdapter
import com.example.vinilos_app.viewmodels.CollectorsCatalogViewModel

class CollectorsFragment : Fragment(R.layout.collectors_fragment) {

    private lateinit var collectorsCatalogViewModel: CollectorsCatalogViewModel
    private lateinit var adapter: CollectorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia de CollectorsRepository
        val repository = CollectorsRepository(requireContext())

        // Usar ViewModelProvider para inicializar CollectorsCatalogViewModel con argumentos
        collectorsCatalogViewModel = ViewModelProvider(
            this,
            CollectorsCatalogViewModelFactory(repository)
        ).get(CollectorsCatalogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_collectors)

        // Configura el adaptador con el listener para manejar clics
        adapter = CollectorsAdapter(emptyList()) { collector ->
            navigateToCollectorDetail(collector.collectorId)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Update UI with collectors list
        collectorsCatalogViewModel.collectorsCatalog.observe(viewLifecycleOwner) { collectorsList ->
            adapter.updateCollectors(collectorsList)
        }

        // Observe errors and show toast
        collectorsCatalogViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Load collectors catalog
        collectorsCatalogViewModel.loadCollectorsCatalog()
    }

    private fun navigateToCollectorDetail(collectorId: Int) {
        val action = CollectorsFragmentDirections.actionCollectorsFragmentToCollectorDetailFragment(collectorId)
        findNavController().navigate(action)
    }
}

// Clase de fábrica para crear CollectorsCatalogViewModel con argumentos
class CollectorsCatalogViewModelFactory(private val repository: CollectorsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectorsCatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectorsCatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
