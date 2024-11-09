package com.example.vinilos_app.repository

import android.content.Context
import com.example.vinilos_app.models.Collector
import com.example.vinilos_app.network.NetworkServiceAdapter

class CollectorsRepository(private val context: Context) {

    private val networkService = NetworkServiceAdapter.getInstance(context)

    // Función para obtener la lista de Collectors
    suspend fun getCollectorsCatalog(): List<Collector> {
        return networkService.getCollectors()
    }
}
