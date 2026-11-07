package se.apps.arctic.prismat.domain.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.data.local.PrismatDataStore
import se.apps.arctic.prismat.domain.model.Constants.LOGCAT_FILTER
import se.apps.arctic.prismat.domain.model.Discount
import se.apps.arctic.prismat.domain.model.FilterType

class FilterViewModel(
    private val allDiscounts: List<Discount>,
    defaultFilter: FilterType = FilterType.NONE,
    context: Context? = null
) : ViewModel() {
    private val prismatDataStore = context?.let { PrismatDataStore(it) }

    var selectedFilter by mutableStateOf(defaultFilter)
    var selectedDiscountIds by mutableStateOf<Set<Int>>(
        allDiscounts.filter { it.isSelected }.map { it.id }.toSet()
    )
    var favoriteDiscountIds by mutableStateOf<Set<Int>>(
        allDiscounts.filter { it.isFavorite }.map { it.id }.toSet()
    )

    init {
        if (prismatDataStore != null) {
            viewModelScope.launch {
                // 1. Read selected discount IDs smartly based on store.name and store.url
                val loadedSelectedIds = mutableSetOf<Int>()
                val stores = allDiscounts.map { it.store }.toSet()
                for (store in stores) {
                    val storeUrl = prismatDataStore.getStoreUrl(store.name) ?: ""
                    if (storeUrl.isNotEmpty()) {
                        val ids = prismatDataStore.getSelectedDiscountIds(store.name, storeUrl)
                        loadedSelectedIds.addAll(ids)
                    }
                }
                selectedDiscountIds = loadedSelectedIds

                // 2. Read last selected filter
                val savedFilterStr = prismatDataStore.getSelectedFilter()
                selectedFilter = try {
                    FilterType.valueOf(savedFilterStr)
                } catch (e: Exception) {
                    FilterType.ALL
                }
            }
        }
    }

    val filteredDiscounts: List<Discount> by derivedStateOf {
        when (selectedFilter) {
            FilterType.ALL -> allDiscounts.map {
                it.copy(
                    isSelected = selectedDiscountIds.contains(it.id),
                    isFavorite = favoriteDiscountIds.contains(it.id)
                )
            }
            FilterType.SELECTED -> allDiscounts.filter { it.id in selectedDiscountIds }.map {
                it.copy(
                    isSelected = false,
                    isFavorite = favoriteDiscountIds.contains(it.id)
                )
            }
            FilterType.FAVORITE -> allDiscounts.filter { it.id in favoriteDiscountIds }.map {
                it.copy(
                    isSelected = selectedDiscountIds.contains(it.id),
                    isFavorite = false
                )
            }
            else -> emptyList()
        }
    }

    fun setFilter(filterType: FilterType) {
        selectedFilter = filterType
        if (prismatDataStore != null) {
            viewModelScope.launch {
                prismatDataStore.saveSelectedFilter(filterType.name)
            }
        }
    }

    fun toggleDiscountSelection(discountId: Int) {
        selectedDiscountIds = if (selectedDiscountIds.contains(discountId)) {
            selectedDiscountIds - discountId
        } else {
            selectedDiscountIds + discountId
        }

        if (prismatDataStore != null) {
            viewModelScope.launch {
                val discount = allDiscounts.find { it.id == discountId }
                if (discount != null) {
                    val store = discount.store
                    val storeUrl = prismatDataStore.getStoreUrl(store.name) ?: ""
                    val storeItemIds = allDiscounts.filter { it.store == store }.map { it.id }.toSet()
                    val storeSelectedIds = selectedDiscountIds.intersect(storeItemIds)
                    prismatDataStore.saveSelectedDiscountIds(store.name, storeUrl, storeSelectedIds)
                }
            }
        }
    }

    fun toggleDiscountFavorite(discountId: Int) {
        favoriteDiscountIds = if (favoriteDiscountIds.contains(discountId)) {
            favoriteDiscountIds - discountId
        } else {
            favoriteDiscountIds + discountId
        }
    }
}
