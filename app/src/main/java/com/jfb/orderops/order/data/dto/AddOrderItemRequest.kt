package com.jfb.orderops.order.data.dto

data class AddOrderItemRequest(
    val productId: Long,
    val quantity: Int
)