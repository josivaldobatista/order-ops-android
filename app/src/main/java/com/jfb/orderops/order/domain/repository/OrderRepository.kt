package com.jfb.orderops.order.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus

interface OrderRepository {

    suspend fun list(status: OrderStatus? = null): AppResult<List<Order>>
}