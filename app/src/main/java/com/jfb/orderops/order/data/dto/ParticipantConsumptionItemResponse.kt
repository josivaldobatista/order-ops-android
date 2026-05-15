package com.jfb.orderops.order.data.dto

data class ParticipantConsumptionItemResponse(
    val orderItemId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)