package com.ecsolution.prismat.data.remote.provider

import com.ecsolution.prismat.data.remote.endpoints.Endpoints
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APiServiceProvider(baseUrl: String) {
    private val _baseUrl = baseUrl

    fun invoke(): Endpoints =
        Retrofit.Builder()
            .baseUrl(_baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Endpoints::class.java)
}