package com.ecsolution.prismat.data.remote.endpoints

import com.ecsolution.prismat.domain.model.stores.Stores
import retrofit2.http.GET

interface StoresEndpoint {
    @GET("stores.json")
    suspend fun getStores(): Stores

}
