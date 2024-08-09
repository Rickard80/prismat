package com.ecsolution.prismat.domain.model

import com.ecsolution.prismat.domain.SupportedStores

data class Discount(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val price: Double,
    val discount: Int,
    val store: SupportedStores
)
