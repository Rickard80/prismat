package com.ecsolution.prismat.data.remote.endpoints

import com.ecsolution.prismat.domain.model.ProductWillys
import com.ecsolution.prismat.domain.model.stores.Stores
import retrofit2.http.GET
import retrofit2.http.Url

interface Endpoints: StoreEndpoints, WillysEndpoints

interface StoreEndpoints {
    @GET("stores.json")
    suspend fun getStores(): Stores
}

interface WillysEndpoints {
    @GET
    suspend fun getWillysProducts(@Url url: String): ProductWillys
}
