package se.apps.arctic.prismat.data.remote

import se.apps.arctic.prismat.data.remote.endpoints.Endpoints
import se.apps.arctic.prismat.data.remote.provider.APiServiceProvider
import se.apps.arctic.prismat.domain.model.ProductWillys
import se.apps.arctic.prismat.domain.model.stores.StoreItem
import se.apps.arctic.prismat.domain.model.stores.Stores

class ApiService: Endpoints {
    private val baseUrl = "https://rickard80.github.io/prismat/"
    private val service: Endpoints = APiServiceProvider(baseUrl).invoke()

    override suspend fun getStores() = service.getStores()

    override suspend fun getWillysProducts(url: String): ProductWillys = service.getWillysProducts(url)

    fun extractDiscountURLFrom(stores: Stores) = stores.storeItems.map { StoreItem(it.name, it.url) }
}