package com.jfb.orderops.order.data.dto

data class PaymentSplitPreviewResponse(
    val orderId: Long,
    val totalAmount: Double,
    val mode: String,
    val participants: List<PaymentSplitParticipantResponse>
)