package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.models.Track
import com.example.vinilos_app.repository.AlbumRepository
import com.example.vinilos_app.repository.TrackRepository
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val albumRepository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {
    // LiveData para los errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData para el detalle del álbum
    private val _albumDetail = MutableLiveData<Album?>()
    val albumDetail: LiveData<Album?> get() = _albumDetail

    // Método para cargar el detalle del álbum
    fun loadAlbumDetail(albumId: Int) {
        albumRepository.getAlbumDetail(albumId,
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

    // Función para agregar un track al álbum
    fun addTrackToAlbum(albumId: Int, track: Track) {
        viewModelScope.launch {
            try {
                // Llama al repositorio para agregar el track al backend
                val newTrack = trackRepository.addTrackToAlbum(albumId, track)

                // Crea una nueva lista de tracks y actualiza el álbum
                val currentAlbum = _albumDetail.value
                if (currentAlbum != null) {
                    val updatedAlbum = currentAlbum.copy(
                        tracks = currentAlbum.tracks + newTrack // Crea una nueva lista con el track agregado
                    )
                    _albumDetail.postValue(updatedAlbum)
                }
            } catch (e: Exception) {
                _error.postValue("Error al agregar el track: ${e.message}")
            }
        }
    }

    // Método para actualizar el álbum
    fun updateAlbum(newAlbum: Album) {
        _albumDetail.postValue(newAlbum)
    }
}