package se.apps.arctic.prismat

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import se.apps.arctic.prismat.domain.model.ApiState
import se.apps.arctic.prismat.domain.model.Constants
import se.apps.arctic.prismat.domain.viewmodel.DiscountViewModel
import se.apps.arctic.prismat.presentation.discounts.DiscountScreen
import se.apps.arctic.prismat.presentation.errorscreen.ErrorScreen
import se.apps.arctic.prismat.presentation.loadingscreen.LoadingScreen
import se.apps.arctic.prismat.ui.theme.PrismatTheme

class MainActivity : AppCompatActivity() {
    private val discountViewModel = DiscountViewModel()

    private fun setupInternetConnectionMonitor() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val hasInternetConnection = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if (!hasInternetConnection) {
            discountViewModel.apiState = ApiState.NO_INTERNET
            discountViewModel.error = getString(ApiState.NO_INTERNET.getText())
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            if (hasInternet && !discountViewModel.hasDiscounts) {
                loadDiscounts()
            }
        }
    }

    private fun loadDiscounts() {
        lifecycleScope.launch {
            discountViewModel.loadDiscounts()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")
        setupInternetConnectionMonitor()

        setContent {
            PrismatTheme(dynamicColor = false, /*darkTheme = false*/) {
                AppContent(discountViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
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