package com.jfb.orderops.order.data.dto

data class ParticipantConsumptionResponse(
    val id: Long,
    val name: String,
    val totalAmount: Double,
    val items: List<ParticipantConsumptionItemResponse>
)