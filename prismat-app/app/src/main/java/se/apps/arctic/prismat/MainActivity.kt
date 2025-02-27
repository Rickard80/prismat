package se.apps.arctic.prismat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.Constants
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.DiscountScreen
import se.apps.arctic.prismat.presentation.loadingscreen.LoadingScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val discountViewModel = DiscountViewModel()

Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")

        lifecycleScope.launch {
            discountViewModel.loadDiscounts()
        }

        setContent {
            PrismatTheme(dynamicColor = false, /*darkTheme = false*/) {
                when (discountViewModel.apiState) {
                    ApiState.Loading -> LoadingScreen()
                    ApiState.Success -> DiscountScreen(discountViewModel)
                    ApiState.Error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}