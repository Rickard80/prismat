package se.apps.arctic.prismat.presentation.discounts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.components.DiscountItem
import se.apps.arctic.prismat.ui.theme.PrismatTheme

@Composable
fun DiscountScreen(
    discountViewModel: DiscountViewModel
) {
    val discounts = discountViewModel.items.collectAsState()
    val listSpacing = if (isSystemInDarkTheme()) 6.dp else 4.dp

    LazyColumn(
        contentPadding = PaddingValues(top = listSpacing, bottom = listSpacing),
        verticalArrangement = Arrangement.spacedBy(listSpacing),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = discounts.value, key = { item -> item.hashCode() }) { discount ->
            DiscountItem(discount)
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