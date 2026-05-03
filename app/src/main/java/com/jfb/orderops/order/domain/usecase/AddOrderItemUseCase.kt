package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.repository.OrderRepository

class AddOrderItemUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        orderId: Long,
        productId: Long,
        quantity: Int
    ): AppResult<Order> {
        if (orderId <= 0) return AppResult.Error("Pedido inválido.")
        if (productId <= 0) return AppResult.Error("Produto inválido.")
        if (quantity <= 0) return AppResult.Error("Quantidade deve ser maior que zero.")

        return repository.addItem(
            orderId = orderId,
            productId = productId,
            quantity = quantity
        )
    }
}