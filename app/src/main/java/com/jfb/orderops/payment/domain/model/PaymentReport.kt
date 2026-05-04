package com.jfb.orderops.payment.domain.model

data class PaymentReport(
    val total: Double,
    val cash: Double,
    val creditCard: Double,
    val debitCard: Double,
    val pix: Double,
    val ticketAverage: Double
)