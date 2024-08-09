package com.ecsolution.prismat.domain.model.stores

import com.google.gson.annotations.SerializedName

data class Stores(
    val id: String,
    @SerializedName("store_items")
    val storeItems: List<StoreItem>
)