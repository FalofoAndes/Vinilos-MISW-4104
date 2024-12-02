package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinilos_app.models.Track

class SharedAlbumViewModel : ViewModel() {

    private val _trackAdded = MutableLiveData<Track?>()
    val trackAdded: LiveData<Track?> get() = _trackAdded

    // Método para notificar que se agregó un track
    fun notifyTrackAdded(track: Track) {
        _trackAdded.value = track
    }

    // Método para resetear el estado después de observar el cambio
    fun clearTrackAdded() {
        _trackAdded.value = null
    }
}
