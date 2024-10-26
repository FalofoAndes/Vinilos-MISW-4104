
package com.example.vinyls_jetpack_application.network

// ApiClient.kt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "https://backvynils-q6yc.onrender.com/"  // Aquí pones la URL base de tu backend

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}