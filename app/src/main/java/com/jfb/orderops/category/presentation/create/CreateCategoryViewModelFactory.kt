package com.jfb.orderops.category.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.category.domain.usecase.CreateCategoryUseCase

class CreateCategoryViewModelFactory(
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                CreateCategoryViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return CreateCategoryViewModel(
                createCategoryUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}