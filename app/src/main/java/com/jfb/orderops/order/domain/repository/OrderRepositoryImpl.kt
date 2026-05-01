package com.jfb.orderops.order.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.data.dto.toDomain
import com.jfb.orderops.order.data.remote.OrderApi
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val api: OrderApi
) : OrderRepository {

    override suspend fun list(status: OrderStatus?): AppResult<List<Order>> {
        return try {
            val orders = api.list(
                status = status?.takeIf { it != OrderStatus.UNKNOWN }?.name
            ).map { it.toDomain() }

            AppResult.Success(orders)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar pedidos.",
                throwable = e
            )
        }
    }
}