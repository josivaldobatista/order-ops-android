package com.jfb.orderops.payment.data.dto

data class PaymentReportResponse(
    val total: Double,
    val cash: Double,
    val creditCard: Double,
    val debitCard: Double,
    val pix: Double
)