package com.jfb.orderops.payment.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.domain.model.Payment

interface PaymentRepository {

    suspend fun payOrder(
        orderId: Long,
        method: String,
        amount: Double
    ): AppResult<Payment>
}