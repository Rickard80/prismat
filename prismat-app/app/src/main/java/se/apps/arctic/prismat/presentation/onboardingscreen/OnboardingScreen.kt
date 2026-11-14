package se.apps.arctic.prismat.presentation.onboardingscreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.R
import se.apps.arctic.prismat.domain.model.Constants.GOLDEN_RATIO
import se.apps.arctic.prismat.domain.model.FilterType
import se.apps.arctic.prismat.domain.viewmodels.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.DiscountScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme
import se.apps.arctic.prismat.ui.theme.Theme
import se.apps.arctic.prismat.ui.theme.spacing

@Composable
fun OnboardingScreen(filter: FilterType, modifier: Modifier = Modifier) {
    val emptySelectionText = if (filter == FilterType.SELECTED) R.string.onboarding_selected else R.string.onboarding_favorites
    val onboardingImage = if (filter == FilterType.SELECTED) R.drawable.onboarding_tap else R.drawable.onboarding_swipe

    when (filter) {
        FilterType.SELECTED, FilterType.FAVORITE -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.double)
            ) {
                Spacer(modifier = Modifier.weight(1-1f/GOLDEN_RATIO))
                Image(
                    painter = painterResource(id = onboardingImage),
                    contentDescription = "onboarding",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(emptySelectionText),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Theme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f/GOLDEN_RATIO))
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoadingScreenPreview() {
    val context = LocalContext.current
    val discountViewmodel = DiscountViewModel(context)
    val nonSelectedItem = DiscountViewModel.Testing.fakeItem().copy(isSelected = false, isFavorite = false)
    discountViewmodel.overwriteDiscounts(mutableListOf(nonSelectedItem))

    PrismatTheme {
        DiscountScreen(discountViewmodel, FilterType.FAVORITE)
    }
}
