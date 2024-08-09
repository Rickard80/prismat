package com.ecsolution.prismat.presentation.discounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ecsolution.prismat.domain.model.Discount

@Composable
fun DiscountItem(item: Discount) {
    Row(modifier = Modifier
        .background(MaterialTheme.colorScheme.onBackground)
        .fillMaxWidth()
        .padding(16.dp, 8.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
            )
            Text(text = item.store.name)
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${item.price.toString()} kr",
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth()
            )
            Text(text = "${item.discount.toString()} %")
        }
    }
}