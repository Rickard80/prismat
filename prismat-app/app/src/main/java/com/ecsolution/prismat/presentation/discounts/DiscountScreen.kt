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
        Discount(0, "äpplen", "frukt", 10f,13, "Willys"),
        Discount(0, "päron", "frukt", 10f,13, "Willys"),
        Discount(0, "citron", "frukt", 10f,13, "Willys"),
        Discount(0, "banan", "frukt", 10f,13, "Willys"),
        Discount(0, "mango", "frukt", 10f,13, "Willys"),
        Discount(0, "melon", "frukt", 10f,13, "Willys"),
        Discount(0, "körsbär", "frukt", 10f,13, "Willys"),
        Discount(0, "något", null, 10f,13, "Willys"),
        Discount(0, "gurka", "grönsak", 10f,13, "Willys"),
        Discount(0, "tomat", "frukt", 10f,13, "Willys"),
        Discount(0, "aubergine", "grönsak", 10f,13, "Willys"),
        Discount(0, "körsbär", "frukt", 10f,13, "Willys"),
        Discount(0, "något", "", 10f,13, "Willys"),
        Discount(0, "gurka", "grönsak", 10f,13, "Willys"),
        Discount(0, "tomat", "frukt", 10f,13, "Willys"),
        Discount(0, "aubergine", "grönsak", 10f,13, "Willys"),
        Discount(0, "körsbär", "frukt", 10f,13, "Willys"),
        Discount(0, "något", null, 10f,13, "Willys"),
        Discount(0, "gurka", "grönsak", 10f,13, "Willys"),
        Discount(0, "tomat", "frukt", 10f,13, "Willys"),
        Discount(0, "aubergine", "grönsak", 10f,13, "Willys"),
        Discount(0, "sallad", "grönsak", 110f,13, "Willys")
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