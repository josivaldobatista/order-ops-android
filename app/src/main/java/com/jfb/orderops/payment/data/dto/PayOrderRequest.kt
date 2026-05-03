package com.jfb.orderops.payment.data.dto

import java.math.BigDecimal

data class PayOrderRequest(
    val method: String,
    val amount: BigDecimal
)