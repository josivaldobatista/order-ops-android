package com.jfb.orderops.product.domain.model

data class Product(
    val id: Long,
    val name: String,
    val categoryId: Long?,
    val categoryName: String?,
    val description: String?,
    val price: Double,
    val active: Boolean
)