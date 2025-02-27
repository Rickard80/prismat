package se.apps.arctic.prismat.data.remote.provider

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.apps.arctic.prismat.data.remote.endpoints.Endpoints

class APiServiceProvider(baseUrl: String) {
    private val _baseUrl = baseUrl

    fun invoke(): Endpoints =
        Retrofit.Builder()
            .baseUrl(_baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Endpoints::class.java)
}