package com.jfb.orderops.order.presentation.state

import com.jfb.orderops.order.domain.model.Order

data class OrderDetailUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val errorMessage: String? = null
)