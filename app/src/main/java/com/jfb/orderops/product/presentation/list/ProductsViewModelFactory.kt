package com.jfb.orderops.product.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase

class ProductsViewModelFactory(
    private val listProductsUseCase: ListProductsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsViewModel(listProductsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}