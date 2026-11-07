package se.apps.arctic.prismat.domain.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.data.local.PrismatDataStore
import se.apps.arctic.prismat.domain.model.Discount

enum class FilterType {
    ALL,
    SELECTED,
    FAVORITE
}

class FilterViewModel(
    private val allDiscounts: List<Discount>,
    private val prismatDataStore: PrismatDataStore? = null,
    private val storeName: String = "WILLYS",
    private val storeUrl: String = "",
    initialFilter: FilterType = FilterType.ALL,
    initialSelectedDiscountIds: Set<Int> = emptySet()
) : ViewModel() {
    var selectedFilter by mutableStateOf(initialFilter)
    var selectedDiscountIds by mutableStateOf<Set<Int>>(initialSelectedDiscountIds)
    var favoriteDiscountIds by mutableStateOf<Set<Int>>(
        allDiscounts.filter { it.isFavorite }.map { it.id }.toSet()
    )

    init {
        if (prismatDataStore != null && initialFilter == FilterType.ALL && initialSelectedDiscountIds.isEmpty()) {
            viewModelScope.launch {
                val savedFilterStr = prismatDataStore.getSelectedFilter()
                selectedFilter = try {
                    FilterType.valueOf(savedFilterStr)
                } catch (e: Exception) {
                    FilterType.ALL
                }

                selectedDiscountIds = prismatDataStore.getSelectedDiscountIds(storeName, storeUrl)
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
                prismatDataStore.saveSelectedDiscountIds(storeName, storeUrl, selectedDiscountIds)
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

    fun saveFilterSettings() {
        if (prismatDataStore != null) {
            viewModelScope.launch {
                prismatDataStore.saveSelectedFilter(selectedFilter.name)
                prismatDataStore.saveSelectedDiscountIds(storeName, storeUrl, selectedDiscountIds)
            }
        }
    }
}
