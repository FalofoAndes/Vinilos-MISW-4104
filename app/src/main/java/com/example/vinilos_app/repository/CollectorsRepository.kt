package com.example.vinilos_app.repository

import android.content.Context
import com.android.volley.VolleyError
import com.example.vinilos_app.models.Collector
import com.example.vinilos_app.models.CollectorDetail
import com.example.vinilos_app.network.NetworkServiceAdapter

class CollectorsRepository(private val context: Context) {

    private val networkService = NetworkServiceAdapter.getInstance(context)

    // Función para obtener la lista de Collectors
    suspend fun getCollectorsCatalog(): List<Collector> {
        return try {
            networkService.getCollectors()
        } catch (error: VolleyError) {
            throw error // Re-lanza el error para que ViewModel lo maneje
        }
    }

    // Función para obtener los detalles de un Collector
    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail {
        return try {
            networkService.getCollectorDetail(collectorId)
        } catch (error: VolleyError) {
            throw error // Re-lanza el error para que ViewModel lo maneje
        }
    }
}
