package com.jfb.orderops.category.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.category.domain.usecase.UpdateCategoryUseCase

class CategoriesViewModelFactory(
    private val listCategoriesUseCase: ListCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(
                listCategoriesUseCase = listCategoriesUseCase,
                updateCategoryUseCase = updateCategoryUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}