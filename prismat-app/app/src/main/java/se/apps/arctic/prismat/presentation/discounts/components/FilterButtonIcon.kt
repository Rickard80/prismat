package se.apps.arctic.prismat.presentation.discounts.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.R
import se.apps.arctic.prismat.presentation.discounts.FilterType

@Composable
fun FilterButtonIcon(filterType: FilterType) {
    Icon(
        painter = painterResource(id = when (filterType) {
            FilterType.ALL -> R.drawable.list_dash
            FilterType.SELECTED -> R.drawable.square_and_pencil
            FilterType.FAVORITE -> R.drawable.star_fill
        }),
        contentDescription = filterType.name,
        tint = Color.White,
        modifier = Modifier.size(18.dp)
    )
}