package com.jfb.orderops.product.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.product.domain.usecase.CreateProductUseCase

class CreateProductViewModelFactory(
    private val createProductUseCase: CreateProductUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProductViewModel(
                createProductUseCase = createProductUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}