package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.repository.OrderRepository

class RemoveOrderItemUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        orderId: Long,
        itemId: Long
    ): AppResult<Order> {
        if (orderId <= 0) return AppResult.Error("Pedido inválido.")
        if (itemId <= 0) return AppResult.Error("Item inválido.")

        return repository.removeItem(
            orderId = orderId,
            itemId = itemId
        )
    }
}