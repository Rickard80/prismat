package com.ecsolution.prismat.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(retrofit: Retrofit) {
    private val apiService = retrofit.create(ApiService::class.java)

    // https://rickard80.github.io/prismat/discounts.json
    suspend fun <T> makeApiRequest(apiCall: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            apiCall.invoke()
        }
    }

    fun WillysRetrofitClient() =
        Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

