package se.apps.arctic.prismat.domain.model.stores

import com.google.gson.annotations.SerializedName
import se.apps.arctic.prismat.domain.SupportedStores

data class StoreItem(
    @SerializedName("name")
    val name: SupportedStores,
    val url: String
)