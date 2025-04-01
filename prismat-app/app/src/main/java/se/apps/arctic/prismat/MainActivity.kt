package se.apps.arctic.prismat

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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
    private val discountViewModel = DiscountViewModel()

    private fun setupInternetConnectionMonitor() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Set up callback
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val hasInternetConnection = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if (!hasInternetConnection) {
            discountViewModel.apiState = ApiState.NO_INTERNET
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)

            if (unmetered && !discountViewModel.hasDiscounts) {
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
        super.onCreate(savedInstanceState)

Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")
        setupInternetConnectionMonitor()

        setContent {
            PrismatTheme(dynamicColor = false, /*darkTheme = false*/) {
                when (discountViewModel.apiState) {
                    ApiState.LOADING -> LoadingScreen()
                    ApiState.SUCCESS -> DiscountScreen(discountViewModel)
                    ApiState.ERROR -> Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show()
                    ApiState.NO_INTERNET -> Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}