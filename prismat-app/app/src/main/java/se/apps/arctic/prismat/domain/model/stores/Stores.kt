package se.apps.arctic.prismat.domain.model

    .stores

import com.google.gson.annotations.SerializedName
import se.apps.arctic.prismat.domain.model.stores.StoreItem

data class Stores(
    val id: String,
    @SerializedName("store_items")
    val storeItems: List<StoreItem>
)