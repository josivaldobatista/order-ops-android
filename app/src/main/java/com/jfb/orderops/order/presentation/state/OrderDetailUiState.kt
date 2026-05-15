package com.jfb.orderops.order.presentation.state

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.model.OrderParticipantConsumptionPreview
import com.jfb.orderops.product.domain.model.Product

data class OrderDetailUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val products: List<Product> = emptyList(),
    val participants: List<OrderParticipant> = emptyList(),
    val consumptionPreview: OrderParticipantConsumptionPreview? = null,
    val newParticipantName: String = "",
    val isCreatingParticipant: Boolean = false,
    val errorMessage: String? = null
)