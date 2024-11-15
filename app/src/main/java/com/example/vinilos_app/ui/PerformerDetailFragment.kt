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
import com.example.vinilos_app.repository.PerformerRepository
import com.example.vinilos_app.ui.adapter.TrackAdapter
import com.example.vinilos_app.viewmodels.PerformerDetailViewModel
import com.example.vinyls_jetpack_application.databinding.PerformerDetailItemBinding

class PerformerDetailFragment  : Fragment() {

    private lateinit var performerDetailViewModel: PerformerDetailViewModel
    private lateinit var trackAdapter: TrackAdapter
    private var performerId: Int? = null
    private lateinit var binding: PerformerDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: PerformerDetailFragmentArgs by navArgs()
        performerId = args.idPerformer
        val repository = PerformerRepository(requireContext())
        performerDetailViewModel = ViewModelProvider(this, PerformerDetailViewModelFactory(repository))
            .get(PerformerDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PerformerDetailItemBinding.inflate(inflater, container, false)


        // Observa el detalle del álbum
        performerDetailViewModel.performerDetail.observe(viewLifecycleOwner, Observer { performer ->
            performer?.let {
                binding.performer = it

            }
        })

        // Observa errores
        performerDetailViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Cargar los detalles del álbum
        performerId?.let { performerDetailViewModel.loadAPerformerDetail(it) }

        return binding.root
    }
}

class PerformerDetailViewModelFactory(private val repository: PerformerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformerDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformerDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}