package com.ecsolution.prismat.domain.model

import com.ecsolution.prismat.domain.model.willys.PaginationData

data class ProductWillys(
    val paginationData: PaginationData,
    val title: String
)