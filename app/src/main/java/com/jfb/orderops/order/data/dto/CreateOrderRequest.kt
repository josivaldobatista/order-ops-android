package com.jfb.orderops.order.data.dto

data class CreateOrderRequest(
    val serviceTableId: Long?,
    val fulfillmentType: String,
    val items: List<CreateOrderItemRequest>
)

data class CreateOrderItemRequest(
    val productId: Long,
    val quantity: Int
)