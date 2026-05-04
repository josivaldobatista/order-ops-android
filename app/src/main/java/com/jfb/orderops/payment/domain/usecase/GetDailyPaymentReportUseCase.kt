package com.jfb.orderops.payment.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.domain.model.DailyPaymentReport
import com.jfb.orderops.payment.domain.repository.PaymentRepository

class GetDailyPaymentReportUseCase(
    private val repository: PaymentRepository
) {

    suspend fun execute(
        start: String,
        end: String
    ): AppResult<List<DailyPaymentReport>> {
        if (start.isBlank() || end.isBlank()) {
            return AppResult.Error("Período inválido.")
        }

        return repository.getDailyReport(
            start = start,
            end = end
        )
    }
}