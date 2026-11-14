package se.apps.arctic.prismat.domain.viewmodels

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.data.local.PrismatDataStore
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
    var selectedFlashTrigger by mutableStateOf(0L)
    var favoriteFlashTrigger by mutableStateOf(0L)

    init {
        if (prismatDataStore != null) {
            viewModelScope.launch {
                // Read saved discount IDs based on store.name
                val loadedSelectedIds = mutableSetOf<Int>()
                val loadedFavoritesIds = mutableSetOf<Int>()
                val stores = allDiscounts.map { it.store }.toSet()

                for (store in stores) {
                    val selectedIds = prismatDataStore.getSelectedDiscountIds(store.name)
                    loadedSelectedIds.addAll(selectedIds)
                    val favoritesIds = prismatDataStore.getFavoriteDiscountIds(store.name)
                    loadedFavoritesIds.addAll(favoritesIds)
                }

                selectedDiscountIds = loadedSelectedIds
                favoriteDiscountIds = loadedFavoritesIds

                // Read last selected filter
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
        val discount = filteredDiscounts.find { it.id == discountId } ?: allDiscounts.find { it.id == discountId }

        val isAdding = !selectedDiscountIds.contains(discountId)
        selectedDiscountIds = if (isAdding) {
            selectedDiscountIds + discountId
        } else {
            selectedDiscountIds - discountId
        }

        if (isAdding) {
            selectedFlashTrigger = System.currentTimeMillis()
        }

        if (prismatDataStore != null && discount != null) {
            viewModelScope.launch {
                prismatDataStore.saveSelectedDiscountIds(discount.store.name, selectedDiscountIds)
            }
        }
    }

    fun toggleDiscountFavorite(discountId: Int) {
        val discount = filteredDiscounts.find { it.id == discountId } ?: allDiscounts.find { it.id == discountId }

        val isAdding = !favoriteDiscountIds.contains(discountId)
        favoriteDiscountIds = if (isAdding) {
            favoriteDiscountIds + discountId
        } else {
            favoriteDiscountIds - discountId
        }

        if (isAdding) {
            favoriteFlashTrigger = System.currentTimeMillis()
        }

        if (prismatDataStore != null && discount != null) {
            viewModelScope.launch {
                prismatDataStore.saveFavoriteDiscountIds(discount.store.name, favoriteDiscountIds)
            }
        }
    }
}
