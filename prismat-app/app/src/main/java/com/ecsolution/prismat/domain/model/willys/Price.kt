package com.ecsolution.prismat.domain.model.willys

data class Price(
    val currencyIso: String,
    val formattedValue: String,
    val maxQuantity: Any,
    val minQuantity: Any,
    val priceType: String,
    val value: Double
)