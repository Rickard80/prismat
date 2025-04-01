package se.apps.arctic.prismat.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.apps.arctic.prismat.data.remote.ApiService
import se.apps.arctic.prismat.domain.SupportedStores
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.Constants
import se.apps.arctic.prismat.domain.model.Discount
import se.apps.arctic.prismat.domain.model.ProductWillys
import se.apps.arctic.prismat.domain.model.stores.StoreItem
import java.io.IOException
import java.util.UUID
import kotlin.math.roundToInt

class DiscountViewModel: ViewModel() {
    private var _items = MutableStateFlow(mutableListOf<Discount>())
    private val apiService = ApiService()
    private var gettingStores = false

    val items: StateFlow<MutableList<Discount>> = _items
    var apiState by mutableStateOf(ApiState.LOADING)
    val hasDiscounts get() = _items.value.isNotEmpty()

    suspend fun loadDiscounts() {
        if (gettingStores) { return }

        gettingStores = true

        try {
            val apiService = ApiService()
            val stores = apiService.getStores()

            val storeItems = apiService.extractDiscountURLFrom(stores)

            val jobs = storeItems.map { storeItem ->
                fetchDiscounts(storeItem)
            }

            jobs.forEach { discounts ->
                _items.value.addAll(discounts)
            }

            _items.value.sortWith(compareBy { it.title} )

            apiState = ApiState.SUCCESS
            gettingStores = false
        } catch (e: Exception) {
            Log.d(Constants.LOGCAT_FILTER, e.message.toString())
            apiState = ApiState.ERROR
            gettingStores = false
        }
    }

    private suspend fun fetchDiscounts(storeItem: StoreItem): List<Discount> {
        when (storeItem.name) {
            SupportedStores.WILLYS -> { return fetchWillysDiscounts(storeItem.url) }
        }
    }

    private suspend fun fetchWillysDiscounts(url: String): List<Discount> {
        val product: ProductWillys

        Log.d(Constants.LOGCAT_FILTER, "URL: $url")
        try {
            product = apiService.getWillysProducts(url)
        } catch (e: Exception) {
            Log.d("fetchWillysDiscounts", e.message.toString())
            apiState = ApiState.ERROR
            return emptyList()
        }

        val list = mutableListOf<Discount>()

        product.paginationData.items.forEach {
            if (it.savingsAmount != 0.0) {
                val savedPrice = it.priceValue - it.savingsAmount
                val percentage = 100 - (savedPrice / it.priceValue * 100).roundToInt()

//                Log.d(Constants.LOGCAT_FILTER, "fetchWillysDiscounts (${it.name}): $it")
                val promo = it.potentialPromotions.first()
                val hasSpecialOffer = promo.conditionLabelFormatted.isNotEmpty()
                val specialOffer = "${promo.conditionLabelFormatted} ${promo.rewardLabel}".replace("+pant", "").trim()
                var comparePrice = it.comparePrice
                var unit = "---"

                if (!promo.comparePrice.isNullOrEmpty()) {
                    unit = if (promo.comparePrice.endsWith(it.comparePriceUnit)) { "" } else "/" + it.comparePriceUnit
                    comparePrice = promo.comparePrice.ifEmpty { it.comparePrice }
                }

                list.add(Discount(
                    id = UUID.randomUUID().hashCode(),
                    title = it.name,
                    subtitle = it.productLine2,
                    price = if (hasSpecialOffer) { specialOffer } else savedPrice.roundToInt().toString() + it.priceUnit,
                    discount = percentage,
                    comparePrice = "${comparePrice}${unit}",
                    store = SupportedStores.WILLYS
                ))
            }
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

    fun overwriteDiscounts(list: List<Discount>) {
        _items.value = list.toMutableList()
    }

    object Testing {
        private fun randomNumber(min: Int = 0, max: Int = 100) = (Math.random() * (max - min) + min).roundToInt()
        private fun getFruits() = listOf("Banana", "Apple", "Orange", "Mango")

        fun fakeItem() = Discount(
            id = 0,
            title = getFruits().random(),
            subtitle = "Frukt",
            price = randomNumber().toString(),
            discount = randomNumber(),
            comparePrice = randomNumber().toString(),
            store = SupportedStores.WILLYS
        )

        fun fakeList(number: Int = 10) : List<Discount> {
            val list = mutableListOf<Discount>()
            for (i in 0..number) {
                list.add(fakeItem())
            }
            return list
        }
    }
}