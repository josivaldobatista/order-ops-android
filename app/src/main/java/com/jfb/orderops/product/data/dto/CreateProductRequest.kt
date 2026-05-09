package com.jfb.orderops.product.data.dto

import java.math.BigDecimal

data class CreateProductRequest(
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val categoryId: Long?,
    val active: Boolean
)