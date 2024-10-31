package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.repository.AlbumRepository

class AlbumCatalogViewModel(private val repository: AlbumRepository) : ViewModel() {

    // LiveData para el catálogo de álbumes
    private val _albumCatalog = MutableLiveData<List<Album>>()
    val albumCatalog: LiveData<List<Album>> get() = _albumCatalog

    // LiveData para los errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Función para cargar el catálogo de álbumes
    fun loadAlbumCatalog() {
        repository.getAlbumCatalog(
            onComplete = { albums ->
                if (albums.isNotEmpty()) {
                    _albumCatalog.postValue(albums)
                } else {
                    _error.postValue("No hay álbumes disponibles.")
                }
            },
            onError = { volleyError ->
                _error.postValue("Error al cargar el catálogo: ${volleyError.message ?: "Error desconocido"}")
            }
        )
    }
}
