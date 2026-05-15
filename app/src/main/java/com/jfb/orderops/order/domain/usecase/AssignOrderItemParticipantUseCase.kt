package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.repository.OrderRepository

class AssignOrderItemParticipantUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        orderId: Long,
        itemId: Long,
        participantId: Long?
    ): AppResult<Order> {
        return repository.assignItemParticipant(
            orderId = orderId,
            itemId = itemId,
            participantId = participantId
        )
    }
}