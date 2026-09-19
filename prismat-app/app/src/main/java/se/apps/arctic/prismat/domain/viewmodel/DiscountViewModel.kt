package se.apps.arctic.prismat.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.getString
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
import java.util.UUID
import kotlin.math.roundToInt

class DiscountViewModel: ViewModel() {
    private var _items = MutableStateFlow<List<Discount>>(emptyList())
    private val apiService = ApiService()
    private var gettingStores = false

    val items: StateFlow<List<Discount>> = _items
    var apiState by mutableStateOf(ApiState.LOADING)
    var error by mutableStateOf("")
    val hasDiscounts get() = _items.value.isNotEmpty()

    suspend fun loadDiscounts() {
        if (gettingStores) { return }

        gettingStores = true
        error = ""
        apiState = ApiState.LOADING

        try {
            val apiService = ApiService()
            val stores = apiService.getStores()

            val storeItems = apiService.extractDiscountURLFrom(stores)

            val jobs = storeItems.map { storeItem ->
                fetchDiscounts(storeItem)
            }

            val allDiscounts = mutableListOf<Discount>()
            jobs.forEach { discounts ->
                allDiscounts.addAll(discounts)
            }

            allDiscounts.sortWith(compareBy { it.title })
            _items.value = allDiscounts

            apiState = ApiState.SUCCESS
            gettingStores = false
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d(Constants.LOGCAT_FILTER, it) }
            error = "loading discounts: " + (e.localizedMessage?.toString() ?: ApiState.ERROR.toString())
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
            error = "fetching discounts: " + (e.localizedMessage?.toString() ?: ApiState.ERROR.toString())
            apiState = ApiState.ERROR
            return emptyList()
        }

        val list = mutableListOf<Discount>()

        product.paginationData.items.forEach {
            if (it.savingsAmount != 0.0) {
                val promo = it.potentialPromotions.first()
                val hasSpecialOffer = promo.conditionLabelFormatted.isNotEmpty()
                val quantity = if (promo.qualifyingCount == 0) 1 else promo.qualifyingCount

                val price = it.priceValue * quantity
                val savedPrice = price - Math.abs(it.savingsAmount)
                val percentage = 100 - (savedPrice / price * 100).roundToInt()

                // Log.d(Constants.LOGCAT_FILTER, "fetchWillysDiscounts (${it.name}): $it")
                val specialOffer = "${promo.conditionLabelFormatted} ${promo.rewardLabel}".replace("+pant", "").trim()
                var comparePrice = it.comparePrice
                var unit = promo.conditionLabel

                if (!promo.comparePrice.isNullOrEmpty()) {
                    unit = if (promo.comparePrice.endsWith(it.comparePriceUnit)) { "" } else "/" + it.comparePriceUnit
                    comparePrice = promo.comparePrice.ifEmpty { it.comparePrice }
                }

                list.add(Discount(
                    id = UUID.randomUUID().hashCode(),
                    title = it.name,
                    subtitle = it.productLine2,
                    price = if (hasSpecialOffer) { specialOffer } else "${savedPrice.roundToInt()} ${it.priceUnit}",
                    discount = percentage,
                    comparePrice = "${comparePrice}${unit}",
                    store = SupportedStores.WILLYS
                ))
            }
        }

        return list
    }

    fun addDiscount(discount: Discount) {
        _items.value = _items.value + discount
    }

    fun removeDiscount(discount: Discount) {
        _items.value = _items.value - discount
    }

    fun getDiscounts() = items

    fun getDiscount(index: Int) = _items.value[index]

    fun clearDiscounts() {
        _items.value = emptyList()
    }

    fun updateDiscount(discount: Discount) {
        val index = _items.value.indexOf(discount)
        if (index != -1) {
            updateDiscount(index, discount)
        }
    }

    fun updateDiscount(index: Int, discount: Discount) {
        val list = _items.value.toMutableList()
        list[index] = discount
        _items.value = list
    }

    fun overwriteDiscounts(list: List<Discount>) {
        _items.value = list
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