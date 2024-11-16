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
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.repository.PerformerRepository
import com.example.vinilos_app.ui.adapter.PerformerAdapter
import com.example.vinilos_app.viewmodels.PerformerCatalogViewModel
import com.example.vinyls_jetpack_application.R



class PerformerFragment : Fragment(R.layout.performer_fragment) {

    private lateinit var performerCatalogViewModel: PerformerCatalogViewModel
    private lateinit var adapter: PerformerAdapter
    private val performerList = listOf<Performer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia de PerformerRepository
        val repository = PerformerRepository(requireContext())

        // Usar ViewModelProvider para inicializar PerformerCatalogViewModel con argumentos
        performerCatalogViewModel = ViewModelProvider(this, PerformerCatalogViewModelFactory(repository))
            .get(PerformerCatalogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView y Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_performers)
        adapter = PerformerAdapter(performerList) { performerId ->
            val action = PerformerFragmentDirections.actionPerformerFragmentToPerformerDetailFragment(performerId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observa la lista de performers y actualiza el adaptador
        performerCatalogViewModel.performerCatalog.observe(viewLifecycleOwner) { performerList ->
            adapter.updatePerformers(performerList)
        }

        // Observa errores y muestra un toast
        performerCatalogViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Cargar el catálogo de performers
        performerCatalogViewModel.loadPerformerCatalog()
    }
}

// Clase de fábrica para crear PerformerCatalogViewModel con argumentos
class PerformerCatalogViewModelFactory(private val repository: PerformerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformerCatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformerCatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
