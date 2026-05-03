package com.jfb.orderops.payment.data.dto

data class PaymentResponse(
    val id: Long,
    val companyId: Long,
    val orderId: Long,
    val method: String,
    val amount: Double,
    val status: String,
    val paidAt: String?,
    val createdAt: String,
    val updatedAt: String
)