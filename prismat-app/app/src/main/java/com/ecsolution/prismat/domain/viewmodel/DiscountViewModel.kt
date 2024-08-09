package com.ecsolution.prismat.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ecsolution.prismat.data.remote.ApiService
import com.ecsolution.prismat.domain.SupportedStores
import com.ecsolution.prismat.domain.model.ApiState
import com.ecsolution.prismat.domain.model.Constants
import com.ecsolution.prismat.domain.model.Discount
import com.ecsolution.prismat.domain.model.stores.StoreItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiscountViewModel: ViewModel() {
    private var _items = MutableStateFlow(mutableListOf<Discount>())
    private val apiService = ApiService()

    val items: StateFlow<MutableList<Discount>> = _items
    var apiState by mutableStateOf(ApiState.Loading)
    private val getDicountCount get() = _items.value.count()
    private val hasDiscounts get() = getDicountCount > 0

    suspend fun loadDiscounts() {
        val apiService = ApiService()
        val stores = apiService.getStores()
        val storeItems = apiService.extractDiscountURLFrom(stores)

        storeItems.forEach {
            _items.value.addAll(fetchDiscounts(it))
        }

        if (hasDiscounts) {
            apiState = ApiState.Success
            Log.d(Constants.LOGCAT_FILTER, "Found ${items.value.count()} discounts")
        }

    }

    private suspend fun fetchDiscounts(storeItem: StoreItem): List<Discount> {
        when (storeItem.name) {
            SupportedStores.WILLYS -> { return fetchWillysDiscounts(storeItem.url) }
        }
    }

    private suspend fun fetchWillysDiscounts(url: String): List<Discount> {
        val product = apiService.getWillysProducts(url)
        val list = mutableListOf<Discount>()

        product.paginationData.items.forEach { willysItem ->
            list.add(Discount(
                0, willysItem.name, willysItem.productLine2, willysItem.priceValue, 0, SupportedStores.WILLYS
            ))
        }

        return list
    }



    fun addDiscount(discount: Discount) {
        _items.value.add(discount)
    }

    fun removeDiscount(discount: Discount) {
        _items.value.remove(discount)
    }

    fun getDiscounts() = items

    fun getDiscount(index: Int) = _items.value[index]

    fun clearDiscounts() {
        _items.value.clear()
    }

    fun updateDiscount(discount: Discount) {
        _items.value[_items.value.indexOf(discount)] = discount
    }

    fun updateDiscount(index: Int, discount: Discount) {
        _items.value[index] = discount
    }
}