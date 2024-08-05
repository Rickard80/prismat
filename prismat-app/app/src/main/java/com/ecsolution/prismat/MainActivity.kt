package com.ecsolution.prismat

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ecsolution.hodor.ui.theme.PrismatTheme
import com.ecsolution.prismat.domain.model.Constants
import com.ecsolution.prismat.presentation.discounts.DiscountScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

Log.d(Constants.LOGCAT_FILTER, "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME} started")

        setContent {
            PrismatTheme() {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DiscountScreen()
                }
            }
        }
    }
}