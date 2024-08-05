package com.ecsolution.prismat.presentation.discounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecsolution.prismat.domain.model.Discount
import com.ecsolution.prismat.presentation.discounts.components.DiscountItem

@Composable
fun DiscountScreen() {
    val discounts = listOf<Discount>(
        Discount(0, "äpplen", "Willys", 10f,13),
        Discount(0, "päron", "Willys", 10f,13),
        Discount(0, "citron", "Willys", 10f,13),
        Discount(0, "banan", "Willys", 10f,13),
        Discount(0, "mango", "Willys", 10f,13),
        Discount(0, "melon", "Willys", 10f,13),
        Discount(0, "körsbär", "Willys", 10f,13),
        Discount(0, "något", "Willys", 10f,13),
        Discount(0, "gurka", "Willys", 10f,13),
        Discount(0, "tomat", "Willys", 10f,13),
        Discount(0, "abougine", "Willys", 10f,13),
        Discount(0, "körsbär", "Willys", 10f,13),
        Discount(0, "något", "Willys", 10f,13),
        Discount(0, "gurka", "Willys", 10f,13),
        Discount(0, "tomat", "Willys", 10f,13),
        Discount(0, "abougine", "Willys", 10f,13),
        Discount(0, "körsbär", "Willys", 10f,13),
        Discount(0, "något", "Willys", 10f,13),
        Discount(0, "gurka", "Willys", 10f,13),
        Discount(0, "tomat", "Willys", 10f,13),
        Discount(0, "abougine", "Willys", 10f,13),
        Discount(0, "sallad", "Willys", 110f,13)
    )

    // Fetching api data and loading it when the screen loads


    return Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(discounts) { discount ->
                DiscountItem(discount)
            }
        }
    }
}