package com.jfb.orderops.payment.data.dto

import com.jfb.orderops.payment.domain.model.DailyPaymentReport

fun DailyPaymentReportResponse.toDomain(): DailyPaymentReport {
    return DailyPaymentReport(
        date = date,
        total = total
    )
}