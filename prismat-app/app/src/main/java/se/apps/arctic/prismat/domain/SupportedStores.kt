package se.apps.arctic.prismat.domain

import com.google.gson.annotations.SerializedName

enum class SupportedStores(val storeName: String) {
    @SerializedName("Willys", alternate = ["willys"])
    WILLYS("Willys"),
}
