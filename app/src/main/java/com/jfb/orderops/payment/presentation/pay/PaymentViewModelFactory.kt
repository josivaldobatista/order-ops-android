package com.jfb.orderops.payment.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.PreviewPaymentSplitUseCase
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase

class PaymentViewModelFactory(
    private val orderId: Long,
    private val amount: Double,
    private val payOrderUseCase: PayOrderUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val previewPaymentSplitUseCase: PreviewPaymentSplitUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(
                orderId = orderId,
                amount = amount,
                payOrderUseCase = payOrderUseCase,
                getOrderByIdUseCase = getOrderByIdUseCase,
                previewPaymentSplitUseCase = previewPaymentSplitUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}