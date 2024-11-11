package com.example.vinilos_app.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinilos_app.models.Performer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.vinilos_app.repository.PerformerRepository
import kotlinx.coroutines.launch

class PerformerCatalogViewModel(private val repository: PerformerRepository) : ViewModel() {

    private val _performerCatalog = MutableLiveData<List<Performer>>()
    val performerCatalog: LiveData<List<Performer>> get() = _performerCatalog

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Función para cargar el catálogo de performers usando corrutinas
    fun loadPerformerCatalog() {
        viewModelScope.launch {
            try {

                val performers = repository.getPerformerCatalog()
                if (performers.isNotEmpty()) {
                    _performerCatalog.postValue(performers)
                } else {
                    _error.postValue("No hay artistas disponibles.")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar el catálogo: ${e.message ?: "Error desconocido"}")
            }
        }
    }
}