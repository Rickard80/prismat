package se.apps.arctic.prismat.presentation.discounts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.components.DiscountItem
import se.apps.arctic.prismat.presentation.discounts.components.FilterBar
import se.apps.arctic.prismat.presentation.errorscreen.ErrorScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme
import se.apps.arctic.prismat.ui.theme.Theme
import se.apps.arctic.prismat.ui.theme.spacing

@Composable
fun DiscountScreen(
    discountViewModel: DiscountViewModel
) {
    val discounts = discountViewModel.items.collectAsStateWithLifecycle()
    
    val currentDiscounts by remember { derivedStateOf { discounts.value } }
    val filterModel = remember { FilterViewModel(currentDiscounts) }

    val listSpacing = if (isSystemInDarkTheme()) Theme.spacing.half else Theme.spacing.quarter

    if (discounts.value.isEmpty()) {
        ErrorScreen(ApiState.NO_DISCOUNTS)
    } else {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            if (filterModel.filteredDiscounts.isEmpty()) {
                if (filterModel.selectedFilter == FilterType.SELECTED) {
                    Text("Click on a discount to select it and it will appear here.")
                } else if (filterModel.selectedFilter == FilterType.FAVORITE) {
                    Text("Favorites is not implemented yet.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(top = listSpacing, bottom = listSpacing),
                    verticalArrangement = Arrangement.spacedBy(listSpacing),
                    modifier = Modifier.weight(1f)
                ) {
                    // Use filterModel.filteredDiscounts instead of original discounts.value
                    items(items = filterModel.filteredDiscounts, key = { item -> item.hashCode() }) { discount ->
                        DiscountItem(
                            item = discount,
                            onClick = { filterModel.toggleDiscountSelection(discount.id) }
                        )
                    }
                }
            }

            FilterBar(filterModel, Modifier.padding(vertical = Theme.spacing.quarter))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscountScreenPreview() {
    val discountViewmodel = DiscountViewModel()
    discountViewmodel.overwriteDiscounts(DiscountViewModel.Testing.fakeList())

    PrismatTheme {
        DiscountScreen(discountViewmodel)
    }
}