package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewResponse
import com.jfb.orderops.order.domain.repository.OrderRepository

class PreviewPaymentSplitUseCase(
    private val repository: OrderRepository
) {

    suspend operator fun invoke(
        orderId: Long,
        numberOfPeople: Int
    ): AppResult<PaymentSplitPreviewResponse> {

        if (numberOfPeople <= 0) {
            return AppResult.Error(
                message = "Número de pessoas inválido."
            )
        }

        return repository.previewPaymentSplit(
            orderId = orderId,
            numberOfPeople = numberOfPeople
        )
    }
}