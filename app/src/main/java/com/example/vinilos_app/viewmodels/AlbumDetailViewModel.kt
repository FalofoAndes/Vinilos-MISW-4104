package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.repository.AlbumRepository

class AlbumDetailViewModel(private val repository: AlbumRepository) : ViewModel() {
    // LiveData para los errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData para el detalle del álbum
    private val _albumDetail = MutableLiveData<Album?>()
    val albumDetail: LiveData<Album?> get() = _albumDetail

    // Función para cargar el detalle de un álbum
    fun loadAlbumDetail(albumId: Int) {
        repository.getAlbumDetail(albumId,
            onComplete = { album ->
                if (album != null) {
                    _albumDetail.postValue(album)
                } else {
                    _error.postValue("No se encontró el álbum.")
                }
            },
            onError = { volleyError ->
                _error.postValue("Error al cargar el detalle del álbum: ${volleyError.message ?: "Error desconocido"}")
            }
        )
    }
}