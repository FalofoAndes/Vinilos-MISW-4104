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
import com.example.vinilos_app.repository.CollectorsRepository
import com.example.vinilos_app.viewmodels.CollectorDetailViewModel
import com.example.vinyls_jetpack_application.databinding.CollectorDetailItemBinding

class CollectorDetailFragment : Fragment() {

    private lateinit var collectorDetailViewModel: CollectorDetailViewModel
    private var collectorId: Int? = null
    private lateinit var binding: CollectorDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtén los argumentos enviados al fragmento
        val args: CollectorDetailFragmentArgs by navArgs()
        collectorId = args.idCollector

        // Configura el ViewModel con el repositorio
        val repository = CollectorsRepository(requireContext())
        collectorDetailViewModel = ViewModelProvider(
            this,
            CollectorDetailViewModelFactory(repository)
        ).get(CollectorDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout y configura el binding
        binding = CollectorDetailItemBinding.inflate(inflater, container, false)

        // Observa los detalles del colector
        collectorDetailViewModel.collectorDetail.observe(viewLifecycleOwner, Observer { collector ->
            collector?.let {
                binding.collector = it
            }
        })

        // Observa errores
        collectorDetailViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Cargar los detalles del colector
        collectorId?.let { collectorDetailViewModel.loadCollectorDetail(it) }

        return binding.root
    }
}

class CollectorDetailViewModelFactory(private val repository: CollectorsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectorDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
