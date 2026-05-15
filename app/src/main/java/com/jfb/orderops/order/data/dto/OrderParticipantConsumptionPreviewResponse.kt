package com.jfb.orderops.order.data.dto

data class OrderParticipantConsumptionPreviewResponse(
    val orderId: Long,
    val totalAmount: Double,
    val participantsTotalAmount: Double,
    val participants: List<ParticipantConsumptionResponse>,
    val unassignedTotalAmount: Double,
    val hasUnassignedItems: Boolean,
    val unassignedItems: List<ParticipantConsumptionItemResponse>
)