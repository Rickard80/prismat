package com.ecsolution.prismat.data.remote

import com.ecsolution.prismat.data.remote.endpoints.StoresEndpoint
import com.ecsolution.prismat.domain.model.stores.Stores
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APiServiceProvider(baseUrl: String) {
    private val _baseUrl = baseUrl

    fun invoke(): Retrofit =
        Retrofit.Builder()
            .baseUrl(_baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

class StoresApiService: StoresEndpoint {
    private val baseUrl = "https://rickard80.github.io/prismat/"
    private val apiService: StoresEndpoint = APiServiceProvider(baseUrl)
        .invoke()
        .create(StoresEndpoint::class.java)

    override suspend fun getStores() = apiService.getStores()

    fun extractDiscountURLFrom(stores: Stores) = stores.store_items.map { it.url }
}