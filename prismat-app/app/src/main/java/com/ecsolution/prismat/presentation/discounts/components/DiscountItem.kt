package com.ecsolution.prismat.presentation.discounts.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ecsolution.prismat.domain.SupportedStores
import com.ecsolution.prismat.domain.model.Discount
import com.ecsolution.prismat.ui.theme.PrismatTheme
import com.ecsolution.prismat.ui.theme.Theme
import com.ecsolution.prismat.ui.theme.spacing

@Composable
fun DiscountItem(item: Discount) {
        val leftHandWeight = 0.7F
        val rightHandWeight = 1 - leftHandWeight

        val colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.secondary,
            leftHandWeight to MaterialTheme.colorScheme.secondary,
            leftHandWeight to MaterialTheme.colorScheme.tertiary,
            1f to MaterialTheme.colorScheme.tertiary
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                Brush.horizontalGradient(
                    colorStops = colorStops
                )
            )
            .padding(vertical = Theme.spacing.half, horizontal = Theme.spacing.normal)
        ) {
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(bottom = 1.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(leftHandWeight)
                )

                Text(text = item.price,
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(rightHandWeight)
                )
            }

            Row(modifier = Modifier.padding(bottom = 1.dp)) {
                item.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(leftHandWeight)
                    )
                }

                Spacer(modifier = Modifier.padding(Theme.spacing.half))

                Text(text = item.comparePrice,
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(rightHandWeight))
            }

            Row {
                Store(item.store.name, Color(0xFF0079FF), modifier = Modifier.weight(1F))

                DiscountSign(discount = item.discount, modifier = Modifier.padding(top =1.dp))
            }
        }
}


@Composable
private fun Store(name: String, color: Color, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(modifier = Modifier
            .background(color, RoundedCornerShape(4.dp))
            .size(6.dp)
        )

        Text(text = name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier
            .padding(start = Theme.spacing.quarter))
    }
}

@Composable
private fun DiscountSign(discount: Int, modifier: Modifier) {
    Box(modifier = modifier
        .background(Color.Red, RoundedCornerShape(4.dp))
        .padding(start = 4.dp, end = 3.dp)
    ) {
        Text(text = "-${discount}%", style = MaterialTheme.typography.labelMedium, letterSpacing = TextUnit(-0.7F, TextUnitType.Sp), color = Color.Yellow, modifier = Modifier
            .align(Alignment.Center)
        )
    }
}

@Preview("Light", showBackground = true)
@Preview("Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiscountItemPreview() {
    PrismatTheme {
        DiscountItem(
            Discount(
                0,
                "Titel som är väldigt lång och tilltagen",
                "Subtitle  ska visa att titel när väldigt lång som tusan",
                "100",
                10,
                "100kr/kg",
                SupportedStores.WILLYS
            )
        )
    }
}