package com.ecsolution.prismat.domain.model.stores

import com.ecsolution.prismat.domain.SupportedStores
import com.google.gson.annotations.SerializedName

data class StoreItem(
    @SerializedName("name")
    val name: SupportedStores,
    val url: String
)