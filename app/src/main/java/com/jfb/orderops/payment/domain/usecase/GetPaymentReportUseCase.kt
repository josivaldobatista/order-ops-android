package com.jfb.orderops.payment.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.domain.model.PaymentReport
import com.jfb.orderops.payment.domain.repository.PaymentRepository

class GetPaymentReportUseCase(
    private val repository: PaymentRepository
) {

    suspend fun execute(
        start: String,
        end: String
    ): AppResult<PaymentReport> {
        if (start.isBlank() || end.isBlank()) {
            return AppResult.Error("Período inválido.")
        }

        return repository.getReport(
            start = start,
            end = end
        )
    }
}