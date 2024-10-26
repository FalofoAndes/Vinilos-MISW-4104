package com.example.vinyls_jetpack_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.HomeBinding

class HomeFragment : Fragment(R.layout.home) {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navegar a AlbumFragment al hacer clic en el selector "Search"
        binding.selectorsDown.selectorSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_albumFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}