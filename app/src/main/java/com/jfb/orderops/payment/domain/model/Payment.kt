package com.jfb.orderops.payment.domain.model

data class Payment(
    val id: Long,
    val orderId: Long,
    val method: PaymentMethod,
    val amount: Double,
    val status: PaymentStatus
)

enum class PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PIX
}

enum class PaymentStatus {
    PENDING,
    PAID,
    CANCELLED,
    UNKNOWN
}