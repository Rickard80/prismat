package se.apps.arctic.prismat.presentation.discounts.components.filterbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.R
import se.apps.arctic.prismat.domain.model.FilterType
import se.apps.arctic.prismat.domain.viewmodels.DiscountViewModel
import se.apps.arctic.prismat.domain.viewmodels.FilterViewModel
import se.apps.arctic.prismat.ui.theme.PrismatTheme

@Composable
fun FilterButtonIcon(filterType: FilterType, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = when (filterType) {
            FilterType.ALL -> R.drawable.list_dash
            FilterType.SELECTED -> R.drawable.square_and_pencil
            FilterType.FAVORITE -> R.drawable.star_fill
            FilterType.NONE -> R.drawable.star_fill
        }),
        contentDescription = filterType.name,
        tint = Color.Unspecified,
        modifier = modifier.fillMaxSize(0.4f)
    )
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterButtonIconPreview() {
    val fakeItems = DiscountViewModel.Testing.fakeList()

    PrismatTheme {
        FilterBar(
            filterViewModel = FilterViewModel(fakeItems),
            modifier = Modifier.padding(5.dp)
        )
    }
}