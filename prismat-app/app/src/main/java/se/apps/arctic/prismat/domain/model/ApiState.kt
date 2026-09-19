package se.apps.arctic.prismat.domain.model

import se.apps.arctic.prismat.R

enum class ApiState {
    LOADING, SUCCESS, ERROR, NO_INTERNET, NO_DISCOUNTS;

    fun getImage(): Int {
        return when (this) {
            ERROR -> R.drawable.xmark_circle
            NO_INTERNET -> R.drawable.wifi_slash
            NO_DISCOUNTS -> R.drawable.list_bullet_below_rectangle
            // NOT USED
            LOADING -> R.drawable.xmark_circle
            SUCCESS -> R.drawable.xmark_circle
        }
    }

    fun getText(): Int {
        return when (this) {
            NO_INTERNET -> R.string.no_internet
            NO_DISCOUNTS -> R.string.no_discounts
            // NOT USED
            ERROR -> R.string.network_error
            LOADING -> R.string.network_error
            SUCCESS -> R.string.network_error
        }
    }
}