package com.ecsolution.prismat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.ecsolution.hodor.ui.theme.PrismatTheme
import com.ecsolution.hodor.ui.theme.background
import com.ecsolution.prismat.domain.model.ApiState
import com.ecsolution.prismat.domain.model.Constants
import com.ecsolution.prismat.domain.viewmodel.DiscountViewModel
import com.ecsolution.prismat.presentation.discounts.DiscountScreen
import com.ecsolution.prismat.presentation.loadingscreen.LoadingScreen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val discountViewModel = DiscountViewModel()

Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")

        lifecycleScope.launch {
            discountViewModel.loadDiscounts()
        }

        setContent {
            PrismatTheme(dynamicColor = true) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    when (discountViewModel.apiState) {
                        ApiState.Loading -> LoadingScreen()
                        ApiState.Success -> DiscountScreen(discountViewModel)
                        ApiState.Error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}