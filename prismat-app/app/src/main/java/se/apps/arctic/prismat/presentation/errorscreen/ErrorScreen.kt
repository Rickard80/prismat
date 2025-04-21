package se.apps.arctic.prismat.presentation.errorscreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.ui.theme.PrismatTheme
import se.apps.arctic.prismat.ui.theme.Theme
import se.apps.arctic.prismat.ui.theme.spacing

@Composable
fun ErrorScreen(
    type: ApiState,
    errorText: String
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(Theme.spacing.normal)
    ) {
        Column(modifier = Modifier.weight(1F).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = type.getImage()),
                contentDescription = stringResource(id = type.getText()),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }

        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .border(width = 3.dp, color = MaterialTheme.colorScheme.errorContainer)
            .padding(Theme.spacing.normal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorText, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorScreenPreview() {
    PrismatTheme {
        ErrorScreen( ApiState.ERROR, "This is an error")
    }
}