package com.jfb.orderops.order.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase

class CreateOrderViewModelFactory(
    private val serviceTableId: Long,
    private val listProductsUseCase: ListProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val listCategoriesUseCase: ListCategoriesUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateOrderViewModel(
                serviceTableId = serviceTableId,
                listProductsUseCase = listProductsUseCase,
                listCategoriesUseCase = listCategoriesUseCase,
                createOrderUseCase = createOrderUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}