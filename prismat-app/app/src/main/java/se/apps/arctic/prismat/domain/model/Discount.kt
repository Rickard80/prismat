package se.apps.arctic.prismat.domain.model

import se.apps.arctic.prismat.domain.SupportedStores

data class Discount(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val price: String,
    val discount: Int,
    val comparePrice: String,
    val store: SupportedStores,
)
