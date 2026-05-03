package com.jfb.orderops.payment.data.dto

import com.jfb.orderops.payment.domain.model.Payment
import com.jfb.orderops.payment.domain.model.PaymentMethod
import com.jfb.orderops.payment.domain.model.PaymentStatus

fun PaymentResponse.toDomain(): Payment {
    return Payment(
        id = id,
        orderId = orderId,
        method = runCatching {
            PaymentMethod.valueOf(method)
        }.getOrDefault(PaymentMethod.CASH),
        amount = amount,
        status = runCatching {
            PaymentStatus.valueOf(status)
        }.getOrDefault(PaymentStatus.UNKNOWN)
    )
}