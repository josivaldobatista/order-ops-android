package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.CreateOrderItem
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.repository.OrderRepository

class CreateOrderUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        serviceTableId: Long,
        items: List<CreateOrderItem>
    ): AppResult<Order> {
        if (serviceTableId <= 0) {
            return AppResult.Error("Mesa inválida.")
        }

        if (items.isEmpty()) {
            return AppResult.Error("Adicione pelo menos um produto ao pedido.")
        }

        if (items.any { it.quantity <= 0 }) {
            return AppResult.Error("A quantidade deve ser maior que zero.")
        }

        return repository.create(
            serviceTableId = serviceTableId,
            items = items
        )
    }
}