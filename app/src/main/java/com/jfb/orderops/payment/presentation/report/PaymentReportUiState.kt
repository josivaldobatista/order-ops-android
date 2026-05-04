package com.jfb.orderops.payment.presentation.report

import com.jfb.orderops.payment.domain.model.PaymentReport
import com.jfb.orderops.payment.domain.model.DailyPaymentReport

data class PaymentReportUiState(
    val report: PaymentReport? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val daily: List<DailyPaymentReport> = emptyList()
)