package com.jfb.orderops.payment.data.dto

import com.jfb.orderops.payment.domain.model.PaymentReport

fun PaymentReportResponse.toDomain(ticketAverage: Double): PaymentReport {
    return PaymentReport(
        total = total,
        cash = cash,
        creditCard = creditCard,
        debitCard = debitCard,
        pix = pix,
        ticketAverage = ticketAverage
    )
}