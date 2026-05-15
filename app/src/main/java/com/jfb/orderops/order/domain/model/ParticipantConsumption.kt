package com.jfb.orderops.order.domain.model

data class ParticipantConsumption(
    val id: Long,
    val name: String,
    val totalAmount: Double,
    val items: List<ParticipantConsumptionItem>
)