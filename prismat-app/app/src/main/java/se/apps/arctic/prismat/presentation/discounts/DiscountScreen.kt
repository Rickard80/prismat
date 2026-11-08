package se.apps.arctic.prismat.presentation.discounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.apps.arctic.prismat.R
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.FilterType
import se.apps.arctic.prismat.domain.viewmodels.DiscountViewModel
import se.apps.arctic.prismat.domain.viewmodels.FilterViewModel
import se.apps.arctic.prismat.presentation.discounts.components.DiscountItem
import se.apps.arctic.prismat.presentation.discounts.components.filterbar.FilterBar
import se.apps.arctic.prismat.presentation.errorscreen.ErrorScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme
import se.apps.arctic.prismat.ui.theme.Theme
import se.apps.arctic.prismat.ui.theme.spacing

@Composable
fun DiscountScreen(
    discountViewModel: DiscountViewModel,
    defaultFilter: FilterType = FilterType.NONE
) {
    val discounts = discountViewModel.items.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val currentDiscounts by remember { derivedStateOf { discounts.value } }
    val filterModel = remember(currentDiscounts) { 
        FilterViewModel(
            allDiscounts = currentDiscounts,
            defaultFilter,
            context = context
        ) 
    }

    val emptySelectionText = when (filterModel.selectedFilter) {
        FilterType.SELECTED -> R.string.onboarding_selected
        FilterType.FAVORITE -> R.string.onboarding_favorites
        else -> 0
    }

    val listStates = remember {
        FilterType.entries.associateWith { LazyListState() }
    }

    if (discounts.value.isEmpty()) {
        ErrorScreen(ApiState.NO_DISCOUNTS)
    } else {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            if (filterModel.filteredDiscounts.isEmpty() && emptySelectionText != 0) {
                Text(
                    text = stringResource(emptySelectionText),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                LazyColumn(
                    state = listStates[filterModel.selectedFilter] ?: rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(Theme.spacing.quarter),
                    modifier = Modifier.weight(1f)
                ) {
                    items(items = filterModel.filteredDiscounts, key = { item -> item.id }) { discount ->
                        DiscountItem(
                            item = discount,
                            onClick = { filterModel.toggleDiscountSelection(discount.id) },
                            onSwipeLeft = { filterModel.toggleDiscountFavorite(discount.id) }
                        )
                    }
                }
            }

            FilterBar(filterModel, Modifier.padding(vertical = Theme.spacing.quarter))
        }
    }
}

@Preview
@Composable
fun DiscountScreenPreview() {
    val context = LocalContext.current
    val discountViewmodel = DiscountViewModel(context)
    discountViewmodel.overwriteDiscounts(DiscountViewModel.Testing.fakeList())

    PrismatTheme {
        DiscountScreen(discountViewmodel, FilterType.ALL)
    }
}
