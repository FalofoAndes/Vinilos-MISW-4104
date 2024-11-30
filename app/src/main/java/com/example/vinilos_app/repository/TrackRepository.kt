package com.example.vinilos_app.repository

import android.content.Context
import com.android.volley.VolleyError
import com.example.vinilos_app.models.Track
import com.example.vinilos_app.network.NetworkServiceAdapter

class TrackRepository(private val context: Context) {

    private val networkService = NetworkServiceAdapter.getInstance(context)

    // Obtener tracks asociados a un álbum
    suspend fun getTracksForAlbum(albumId: Int): List<Track> {
        return networkService.getTracks(albumId)
    }

    // Agregar un track a un álbum
    suspend fun addTrackToAlbum(albumId: Int, track: Track): Track {
        return networkService.addTrack(albumId, track)
    }
}
