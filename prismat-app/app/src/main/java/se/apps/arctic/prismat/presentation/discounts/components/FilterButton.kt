package se.apps.arctic.prismat.presentation.discounts.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.R
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.FilterType
import se.apps.arctic.prismat.presentation.discounts.FilterViewModel
import se.apps.arctic.prismat.ui.theme.PrismatTheme

@Composable
fun FilterButton(
    filterType: FilterType,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick, interactionSource = interactionSource, indication = null)
            .size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = isSelected, animationSpec =  tween(durationMillis = if (isSelected) 0 else 200, easing = EaseOut)) { isSelected ->
            isSelected
            if (isSelected) {
                FilterButtonBackground(R.drawable.filterbutton_selected)
            } else {
                FilterButtonBackground(R.drawable.filterbutton_unselected)
            }
        }

        FilterButtonIcon(filterType)
    }
}

@Composable
fun FilterButtonBackground(drawableResource: Int) {
    Image(
        painter = painterResource(id = drawableResource),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        contentDescription = "background"
    )
}


@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterButtonPreview() {
    val fakeItems = DiscountViewModel.Testing.fakeList()

    PrismatTheme {
        FilterBar(
            filterViewModel = FilterViewModel(fakeItems),
            modifier = Modifier.padding(5.dp)
        )
    }
}