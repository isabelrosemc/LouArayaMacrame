package com.example.applouaraya.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Construye el cliente Retrofit (https + URL base terminada en "/"). Lo usa RetrofitInstance.
object ApiConfig {
    fun crear(baseUrl: String): Retrofit {
        require(baseUrl.startsWith("https://") && baseUrl.endsWith("/"))
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}
