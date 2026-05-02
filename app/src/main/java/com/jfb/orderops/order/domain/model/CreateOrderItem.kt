package com.jfb.orderops.order.domain.model

data class CreateOrderItem(
    val productId: Long,
    val quantity: Int
)