package com.ecsolution.prismat.domain.model

data class Discount(
    val id: Int,
    val title: String,
    val store: String,
    val price: Float,
    val discount: Int
)
