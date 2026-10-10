package se.apps.arctic.prismat.presentation.discounts

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import se.apps.arctic.prismat.domain.model.Discount

enum class FilterType {
    ALL,
    SELECTED,
    FAVORITE
}

class FilterViewModel(private val allDiscounts: List<Discount>) : ViewModel() {
    var selectedFilter by mutableStateOf(FilterType.ALL)
    var selectedDiscountIds by mutableStateOf<Set<Int>>(emptySet())

    val filteredDiscounts: List<Discount> by derivedStateOf {
        when (selectedFilter) {
            FilterType.ALL -> allDiscounts.map { val updatedItem = it.copy(isSelected = selectedDiscountIds.contains(it.id)); updatedItem }
            FilterType.SELECTED -> allDiscounts.filter { it.id in selectedDiscountIds }.map { val updatedItem = it.copy(isSelected = false); updatedItem }
            FilterType.FAVORITE -> allDiscounts.filter { false } // Placeholder for favorite logic
        }
    }

    fun setFilter(filterType: FilterType) {
        selectedFilter = filterType
    }

    fun toggleDiscountSelection(discountId: Int) {
        selectedDiscountIds = if (selectedDiscountIds.contains(discountId)) {
            selectedDiscountIds - discountId // Deselect
        } else {
            selectedDiscountIds + discountId // Select
        }
    }

    // Placeholder for persistence logic (e.g., saving to DataStore or SharedPreferences)
    fun saveFilterSettings() {
        // Implementation for saving selectedFilter and selectedDiscountIds would go here
    }
}