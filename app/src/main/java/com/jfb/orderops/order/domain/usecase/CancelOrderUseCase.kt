package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.repository.OrderRepository

class CancelOrderUseCase(
    private val repository: OrderRepository
) {
    suspend fun execute(id: Long): AppResult<Order> {
        if (id <= 0) return AppResult.Error("Pedido inválido.")
        return repository.cancel(id)
    }
}