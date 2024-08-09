package com.ecsolution.prismat.presentation.discounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ecsolution.prismat.domain.model.Discount
import com.ecsolution.prismat.domain.viewmodel.DiscountViewModel
import com.ecsolution.prismat.presentation.discounts.components.DiscountItem

@Composable
fun DiscountScreen(
    discountViewModel: DiscountViewModel
) {
    val discounts = discountViewModel.items.collectAsState()

    Column(Modifier.background(Color.White)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(items = discounts.value) { discount ->
                DiscountItem(discount)
            }
        }
    }
}