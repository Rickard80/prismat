package se.apps.arctic.prismat.presentation.discounts.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.FilterType
import se.apps.arctic.prismat.presentation.discounts.FilterViewModel
import se.apps.arctic.prismat.ui.theme.PrismatTheme
import se.apps.arctic.prismat.ui.theme.Theme
import se.apps.arctic.prismat.ui.theme.spacing

@Composable
fun FilterBar(
    filterViewModel: FilterViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.double, Alignment.CenterHorizontally),
    ) {
        FilterType.entries.forEach { type ->
            FilterButton(
                filterType = type,
                isSelected = filterViewModel.selectedFilter == type,
                onClick = {
                    filterViewModel.setFilter(type)
                }
            )
        }
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterBarPreview() {
    val fakeItems = DiscountViewModel.Testing.fakeList()

    PrismatTheme {
        FilterBar(
            filterViewModel = FilterViewModel(fakeItems),
            modifier = Modifier.padding(5.dp)
        )
    }
}