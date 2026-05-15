package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.OrderParticipantConsumptionPreview
import com.jfb.orderops.order.domain.repository.OrderRepository

class GetParticipantConsumptionPreviewUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        orderId: Long
    ): AppResult<OrderParticipantConsumptionPreview> {
        return repository.getParticipantConsumptionPreview(orderId)
    }
}