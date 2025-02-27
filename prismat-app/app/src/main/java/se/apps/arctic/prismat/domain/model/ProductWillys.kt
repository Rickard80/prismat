package se.apps.arctic.prismat.domain.model

import se.apps.arctic.prismat.domain.model.willys.PaginationData

data class ProductWillys(
    val paginationData: PaginationData,
    val title: String
)