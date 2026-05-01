package com.jfb.orderops.product.data.dto

data class ProductResponse(
    val id: Long,
    val companyId: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val active: Boolean,
    val createdAt: String,
    val updatedAt: String
)