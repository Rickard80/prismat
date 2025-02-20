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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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

    Column(modifier = Modifier
        .padding(end = Theme.spacing.half, top = Theme.spacing.half, bottom = Theme.spacing.normal, start = Theme.spacing.half)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(end = Theme.spacing.half, top = Theme.spacing.half, bottom = Theme.spacing.normal, start = Theme.spacing.normal)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1F).padding(end = Theme.spacing.half, bottom = 2.dp)
                )

                Text(text = item.price, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }

            Row {
                item.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1F).padding(end = Theme.spacing.half)
                    )
                }

                Spacer(modifier = Modifier.padding(Theme.spacing.half))

                Text(text = item.comparePrice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }

        }

        Store(item.store.name, Color.Cyan)

        DiscountSign(discount = item.discount)
    }
}

@Composable
private fun DiscountSign(discount: Int) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(letterSpacing = TextUnit(-0.7F, TextUnitType.Sp))) {
            append("-$discount")
        }

        withStyle(style = SpanStyle(letterSpacing = TextUnit(1F, TextUnitType.Sp))) {
            append("%")
        }
    }
    Layout(content = {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.error, CircleShape)
            .size(34.dp)
            .padding(bottom = 1.dp)
        ) {
            Text(text = text, style = MaterialTheme.typography.labelMedium, letterSpacing = TextUnit(-0.7F, TextUnitType.Sp), color = Color.Yellow, modifier = Modifier
                .align(Alignment.Center)
            )
        }
    }) { measurables, constraints ->
        val box = measurables[0].measure(constraints)

        layout(0, 0) {
            // Offset the box vertically to half the height of the box
            box.placeRelative(IntOffset(constraints.maxWidth - box.width + 6, -box.height/2 + 3))
        }
    }
}


@Composable
private fun Store(name: String, color: Color) {
    Layout(content = {
        Box(modifier = Modifier
            .background(color, RoundedCornerShape(4.dp))
            .padding(bottom = 1.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                .padding(horizontal = Theme.spacing.half, vertical = 2.dp)
            ) {
                Text(text = name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }) {measurables, constraints ->
        val box = measurables[0].measure(constraints)
        val halfBoxWidth = box.width / 2
        val halfBoxHeight = box.height / -2

        layout(constraints.maxWidth, 0) {
            // Offset the box vertically to half the height of the box
            box.placeRelative(IntOffset(constraints.maxWidth / 2 - halfBoxWidth, halfBoxHeight))
        }
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
                "Titel som är",
                "Subtitle  ska visa att titel när väldigt lång som tusan",
                "100",
                10,
                "100kr/kg",
                SupportedStores.WILLYS
            )
        )
    }
}