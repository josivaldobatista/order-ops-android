package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.domain.repository.OrderRepository

class ListOrdersUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(status: OrderStatus? = null): AppResult<List<Order>> {
        return repository.list(status)
    }
}