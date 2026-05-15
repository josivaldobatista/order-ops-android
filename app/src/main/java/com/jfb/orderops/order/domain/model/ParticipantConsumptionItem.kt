package com.jfb.orderops.order.domain.model

data class ParticipantConsumptionItem(
    val orderItemId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)