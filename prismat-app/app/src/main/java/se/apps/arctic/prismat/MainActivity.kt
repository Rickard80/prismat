package se.apps.arctic.prismat

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.data.NetworkMonitor
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.Constants
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.DiscountScreen
import se.apps.arctic.prismat.presentation.errorscreen.ErrorScreen
import se.apps.arctic.prismat.presentation.loadingscreen.LoadingScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme

class MainActivity : AppCompatActivity() {
    private val discountViewModel: DiscountViewModel by viewModels()
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        networkMonitor = NetworkMonitor(this)
        observeNetwork()

        Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")

        setContent {
            PrismatTheme(dynamicColor = false) {
                AppContent(discountViewModel)
            }
        }
    }

    private fun observeNetwork() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkMonitor.isConnected.collect { isConnected ->
                    if (isConnected) {
                        if (!discountViewModel.hasDiscounts) {
                            loadDiscounts()
                        }
                    } else {
                        discountViewModel.apiState = ApiState.NO_INTERNET
                        discountViewModel.error = getString(ApiState.NO_INTERNET.getText())
                    }
                }
            }
        }
    }

    private fun loadDiscounts() {
        lifecycleScope.launch {
            discountViewModel.loadDiscounts()
        }
    }
}

@Composable
fun AppContent(discountViewModel: DiscountViewModel) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    
    androidx.compose.material3.Surface(
        modifier = Modifier.padding(systemBarsPadding),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        when (discountViewModel.apiState) {
            ApiState.LOADING -> LoadingScreen()
            ApiState.SUCCESS -> DiscountScreen(discountViewModel)
            ApiState.ERROR -> ErrorScreen(ApiState.ERROR)
            ApiState.NO_INTERNET, ApiState.NO_DISCOUNTS -> ErrorScreen(ApiState.NO_INTERNET)
        }
    }
}