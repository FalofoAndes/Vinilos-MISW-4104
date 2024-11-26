package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilos_app.models.Album
import com.example.vinilos_app.repository.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel(private val repository: AlbumRepository) : ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun addAlbum(album: Album, onSuccess: (Album?) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.postAlbum(
                album,
                onComplete = { albumResponse ->
                    if (albumResponse != null) {
                        onSuccess(albumResponse)
                    } else {
                        onFailure("Error creando el álbum.")
                    }
                },
                onError = { error ->
                    onFailure(error.message ?: "Error desconocido")
                }
            )
        }
    }
}