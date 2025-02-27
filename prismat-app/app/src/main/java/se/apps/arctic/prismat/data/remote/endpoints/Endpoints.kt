package se.apps.arctic.prismat.data.remote.endpoints

import retrofit2.http.GET
import retrofit2.http.Url
import se.apps.arctic.prismat.domain.model.ProductWillys
import se.apps.arctic.prismat.domain.model.stores.Stores

interface Endpoints: StoreEndpoints, WillysEndpoints

interface StoreEndpoints {
    @GET("stores.json")
    suspend fun getStores(): Stores
}

interface WillysEndpoints {
    @GET
    suspend fun getWillysProducts(@Url url: String): ProductWillys
}
