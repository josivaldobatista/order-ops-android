package com.jfb.orderops.order.data.dto

data class OrderResponse(
    val id: Long,
    val companyId: Long?,
    val serviceTableId: Long?,
    val fulfillmentType: String?,
    val status: String,
    val totalAmount: Double,
    val items: List<OrderItemResponse>
)