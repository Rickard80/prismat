package se.apps.arctic.prismat.domain.model

import se.apps.arctic.prismat.domain.model.willys.PaginationData

data class StoreWillys(
    val paginationData: PaginationData,
    val title: String
)