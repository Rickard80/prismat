package com.ecsolution.prismat.domain.model

import com.ecsolution.prismat.domain.model.willys.PaginationData

data class WillysItem(
    val paginationData: PaginationData,
    val title: String
)