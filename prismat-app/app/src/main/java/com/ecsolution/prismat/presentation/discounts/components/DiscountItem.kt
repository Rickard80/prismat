package com.ecsolution.prismat.presentation.discounts.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ecsolution.prismat.domain.SupportedStores
import com.ecsolution.prismat.domain.model.Discount

@Composable
fun DiscountItem(item: Discount) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .padding(end = 16.dp, top = 8.dp, bottom = 8.dp, start = 4.dp)
    ) {
        StoreColor(Color.Cyan)

        Column(modifier = Modifier.weight(weight = 0.1f, fill = true).padding(horizontal = 8.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            item.subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(text = item.store.name, style = MaterialTheme.typography.bodySmall)
        }


        Column(horizontalAlignment = Alignment.End) {
            Text(text = item.price, style = MaterialTheme.typography.titleMedium)
            Text(text = item.comparePrice, style = MaterialTheme.typography.bodyMedium)
            Text(text = "-${item.discount} %", style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
private fun StoreColor(color: Color) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .background(color)
            .padding(horizontal = 2.dp)
            .fillMaxHeight()
    )
}

@Preview("Light", showBackground = true)
@Preview("Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiscountItemPreview() {
    DiscountItem(
        Discount(
            0,
            "Titel som är",
            "Subtitle  ska visa att titel när väldigt lång som tusan",
            "100",
            10,
            "100kr/kg",
            SupportedStores.WILLYS
        )
    )
}