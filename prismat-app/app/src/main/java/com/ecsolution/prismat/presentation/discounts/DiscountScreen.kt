package com.ecsolution.prismat.presentation.discounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ecsolution.prismat.domain.viewmodel.DiscountViewModel
import com.ecsolution.prismat.presentation.discounts.components.DiscountItem
import com.ecsolution.prismat.ui.theme.PrismatTheme
import com.ecsolution.prismat.ui.theme.Theme
import com.ecsolution.prismat.ui.theme.spacing

@Composable
fun DiscountScreen(
    discountViewModel: DiscountViewModel
) {
    val discounts = discountViewModel.items.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(top = Theme.spacing.half, bottom = Theme.spacing.normal),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = discounts.value) { discount ->
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