package com.jfb.orderops.order.data.dto

data class CreateOrderRequest(
    val serviceTableId: Long,
    val items: List<CreateOrderItemRequest> = emptyList()
)

data class CreateOrderItemRequest(
    val productId: Long,
    val quantity: Int
)