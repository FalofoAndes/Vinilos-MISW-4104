package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.repository.PerformerRepository

class PerformerDetailViewModel(private val repository: PerformerRepository) : ViewModel() {
    // LiveData para los errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData para el detalle del artista
    private val _performerDetail = MutableLiveData<Performer?>()
    val performerDetail: LiveData<Performer?> get() = _performerDetail

    // Función para cargar el detalle de un artista
    fun loadAPerformerDetail(performerId: Int) {
        repository.getPerformerDetail(performerId,
            onComplete = { performer ->
                if (performer != null) {
                    _performerDetail.postValue(performer)
                } else {
                    _error.postValue("No se encontró el artista.")
                }
            },
            onError = { volleyError ->
                _error.postValue("Error al cargar el detalle del artista: ${volleyError.message ?: "Error desconocido"}")
            }
        )
    }
}