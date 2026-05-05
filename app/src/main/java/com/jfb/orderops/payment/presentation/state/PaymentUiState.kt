package com.jfb.orderops.payment.presentation.state

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.payment.domain.model.PaymentMethod

data class PaymentUiState(
    val orderId: Long = 0,
    val amount: Double = 0.0,
    val order: Order? = null,
    val selectedMethod: PaymentMethod = PaymentMethod.PIX,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)