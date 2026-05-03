package com.jfb.orderops.product.data.dto

data class CreateProductRequest(
    val name: String,
    val description: String?,
    val price: Double
)