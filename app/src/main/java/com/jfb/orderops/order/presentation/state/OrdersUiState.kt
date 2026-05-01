package com.jfb.orderops.order.presentation.state

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus

data class OrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val selectedStatus: OrderStatus? = null,
    val errorMessage: String? = null
)