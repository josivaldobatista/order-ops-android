package com.jfb.orderops.order.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase

class OrderDetailViewModelFactory(
    private val orderId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderDetailViewModel(
                orderId = orderId,
                getOrderByIdUseCase = getOrderByIdUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}