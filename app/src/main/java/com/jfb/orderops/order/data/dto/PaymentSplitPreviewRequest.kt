package com.jfb.orderops.order.data.dto

data class PaymentSplitPreviewRequest(
    val mode: String,
    val numberOfPeople: Int
)