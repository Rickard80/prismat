package com.ecsolution.prismat.domain.model.willys

data class PaginationData(
    val items: List<Item>,
    val links: Links,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int
)