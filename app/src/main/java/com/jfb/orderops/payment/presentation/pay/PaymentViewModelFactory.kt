package com.jfb.orderops.payment.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase

class PaymentViewModelFactory(
    private val orderId: Long,
    private val amount: Double,
    private val payOrderUseCase: PayOrderUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(
                orderId = orderId,
                amount = amount,
                payOrderUseCase = payOrderUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}