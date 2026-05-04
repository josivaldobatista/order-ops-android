package com.jfb.orderops.payment.data.dto

data class DailyPaymentReportResponse(
    val date: String,
    val total: Double
)