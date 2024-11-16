package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.example.vinilos_app.models.Collector
import com.example.vinilos_app.repository.CollectorsRepository
import kotlinx.coroutines.launch

class CollectorsCatalogViewModel(private val repository: CollectorsRepository) : ViewModel() {

    // LiveData para el catálogo de collectors
    private val _collectorsCatalog = MutableLiveData<List<Collector>>()
    val collectorsCatalog: LiveData<List<Collector>> get() = _collectorsCatalog

    // LiveData para los errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Función para cargar el catálogo de collectors
    fun loadCollectorsCatalog() {
        viewModelScope.launch {
            try {
                val collectors = repository.getCollectorsCatalog() // Llamada suspendida
                if (collectors.isNotEmpty()) {
                    _collectorsCatalog.value = collectors
                } else {
                    _error.value = "No hay collectors disponibles."
                }
            } catch (error: VolleyError) {
                handleVolleyError(error)
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.message ?: "Error desconocido"}"
            }
        }
    }

    // Manejo de errores específicos de Volley
    private fun handleVolleyError(error: VolleyError) {
        _error.value = when (error.networkResponse?.statusCode) {
            404 -> "Recurso no encontrado."
            500 -> "Error interno del servidor."
            else -> "Error de red: ${error.message ?: "Error desconocido"}"
        }
    }
}
