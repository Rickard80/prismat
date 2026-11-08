package se.apps.arctic.prismat.domain.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.apps.arctic.prismat.data.local.PrismatDataStore
import se.apps.arctic.prismat.data.remote.ApiService
import se.apps.arctic.prismat.domain.SupportedStores
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.Constants
import se.apps.arctic.prismat.domain.model.Discount
import se.apps.arctic.prismat.domain.model.StoreWillys
import se.apps.arctic.prismat.domain.model.stores.StoreItem
import java.util.UUID
import kotlin.math.abs
import kotlin.math.roundToInt

class DiscountViewModel(context: Context) : ViewModel() {
    val prismatDataStore = PrismatDataStore(context)
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
            val stores = apiService.getStores()
            val storeItems = apiService.extractDiscountURLFrom(stores)

            val allDiscounts = mutableListOf<Discount>()

            for (storeItem in storeItems) {
                val storeName = storeItem.name.name
                val storeUrl = storeItem.url

                // Save store url (handles weekly mismatch cleanup of old URL's discounts and selected IDs)
                prismatDataStore.saveStoreUrl(storeName, storeUrl)

                // Check cached discounts for this storeItem.url first
                var discounts = prismatDataStore.getDiscountsForUrl(storeName)

                if (discounts.isEmpty()) {
                    discounts = fetchDiscounts(storeItem)
                    if (discounts.isNotEmpty()) {
                        prismatDataStore.saveDiscountsForUrl(storeName, discounts)
                    }
                }

                allDiscounts.addAll(discounts)
            }

            allDiscounts.sortWith(compareBy { it.title })
            _items.value = allDiscounts

            apiState = ApiState.SUCCESS
            gettingStores = false
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d(Constants.LOGCAT_FILTER, it) }
            error = "loading discounts: " + (e.localizedMessage ?: ApiState.ERROR.toString())
            apiState = ApiState.ERROR
            gettingStores = false
        }
    }

    private suspend fun fetchDiscounts(storeItem: StoreItem): List<Discount> {
        return when (storeItem.name) {
            SupportedStores.WILLYS -> fetchWillysDiscounts(storeItem.url)
        }
    }

    private suspend fun fetchWillysDiscounts(url: String): List<Discount> {
        return fetchProductsForStore(url, SupportedStores.WILLYS)
    }

    private suspend fun fetchProductsForStore(url: String, store: SupportedStores): List<Discount> {
        val product: StoreWillys

        Log.d(Constants.LOGCAT_FILTER, "URL ($store): $url")
        try {
            product = when (store) {
                SupportedStores.WILLYS -> apiService.getWillysProducts(url)
            }
        } catch (e: Exception) {
            error = "fetching discounts: " + (e.localizedMessage ?: ApiState.ERROR.toString())
            apiState = ApiState.ERROR
            return emptyList()
        }

        val list = mutableListOf<Discount>()

        product.paginationData.items.forEach {
            if (it.savingsAmount != 0.0) {
                val promo = it.potentialPromotions.firstOrNull()
                val hasSpecialOffer = promo?.conditionLabelFormatted?.isNotEmpty() == true
                val quantity = if (promo == null || promo.qualifyingCount == 0) 1 else promo.qualifyingCount

                val price = it.priceValue * quantity
                val savedPrice = price - abs(it.savingsAmount)
                val percentage = 100 - (savedPrice / price * 100).roundToInt()

                val specialOffer = if (promo != null) {
                    "${promo.conditionLabelFormatted} ${promo.rewardLabel}".replace("+pant", "").trim()
                } else {
                    ""
                }
                var comparePrice = it.comparePrice
                var unit = promo?.conditionLabel ?: ""

                if (promo?.comparePrice?.isNotEmpty() == true) {
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
                    store = store,
                    isSelected = false,
                    isFavorite = false
                ))
            }
        }

        return list
    }

    fun overwriteDiscounts(list: List<Discount>) {
        _items.value = list
    }

    /*** TEST DATA FOR PREVIEW ***/

    object Testing {
        private fun randomNumber(min: Int = 0, max: Int = 100) = (Math.random() * (max - min) + min).roundToInt()
        private fun getFruits() = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Kiwi", "Lingonberry", "Mango", "Nectarine", "Orange", "Pear", "Raspberry", "Strawberry", "Tomato", "Watermelon")
        private fun randomStore() = SupportedStores.entries.toTypedArray().random()

        fun fakeItem() = Discount(
            id = 0,
            title = getFruits().random(),
            subtitle = "Frukt",
            price = "${randomNumber()} kr" ,
            discount = randomNumber(),
            comparePrice = "${randomNumber()} kr/kg",
            store = randomStore(),
            isSelected = randomNumber() < 30,
            isFavorite =  randomNumber() < 15
        )

        fun fakeList(number: Int = 10) : List<Discount> {
            val list = mutableListOf<Discount>()
            for (i in 0..number) {
                list.add(fakeItem().copy(id = i))
            }
            return list
        }
    }
}
