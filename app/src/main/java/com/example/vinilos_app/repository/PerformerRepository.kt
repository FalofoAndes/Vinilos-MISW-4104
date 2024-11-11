package com.example.vinilos_app.repository

import android.content.Context
import com.android.volley.VolleyError
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.network.NetworkServiceAdapter

class PerformerRepository(private val context: Context) {

    private val networkService = NetworkServiceAdapter.getInstance(context)

    // Función suspendida para obtener la lista de performers
    suspend fun getPerformerCatalog(): List<Performer> {
        return networkService.getPerformers() // Suponiendo que getPerformers es también una función suspendida
    }
}