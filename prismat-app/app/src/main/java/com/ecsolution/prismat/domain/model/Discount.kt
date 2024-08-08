package com.ecsolution.prismat.domain.model

data class Discount(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val price: Float,
    val discount: Int,
    val store: String
)
