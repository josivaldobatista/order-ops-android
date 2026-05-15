package com.jfb.orderops.order.domain.model

data class OrderParticipantConsumptionPreview(
    val orderId: Long,
    val totalAmount: Double,
    val participantsTotalAmount: Double,
    val participants: List<ParticipantConsumption>,
    val unassignedTotalAmount: Double,
    val hasUnassignedItems: Boolean,
    val unassignedItems: List<ParticipantConsumptionItem>
)