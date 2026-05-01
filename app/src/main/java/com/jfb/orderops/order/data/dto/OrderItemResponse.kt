package com.jfb.orderops.order.data.dto

data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val createdAt: String,
    val updatedAt: String
)