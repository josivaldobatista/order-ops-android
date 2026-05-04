package com.jfb.orderops.payment.domain.model

data class DailyPaymentReport(
    val date: String,
    val total: Double
)