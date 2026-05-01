package com.jfb.orderops.order.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.order.domain.usecase.ListOrdersUseCase

class OrdersViewModelFactory(
    private val listOrdersUseCase: ListOrdersUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrdersViewModel(listOrdersUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}