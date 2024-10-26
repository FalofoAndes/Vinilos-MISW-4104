package com.example.vinyls_jetpack_application.network

// AlbumApiService.kt
import com.example.vinilosapp.models.Album
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path


interface AlbumApiService {
    // Endpoint para obtener la lista de álbumes
    @GET("albums")
    fun getAlbums(): Call<List<Album>>

    // Endpoint para obtener el detalle de un álbum
    @GET("albums/{id}")
    fun getAlbumDetail(@Path("id") albumId: Int): Call<Album>
}