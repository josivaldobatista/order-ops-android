package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.repository.OrderRepository

class ListOrderParticipantsUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(orderId: Long): AppResult<List<OrderParticipant>> {
        return repository.listParticipants(orderId)
    }
}