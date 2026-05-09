package com.jfb.orderops.category.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase

class CategoriesViewModelFactory(
    private val listCategoriesUseCase: ListCategoriesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(
                listCategoriesUseCase = listCategoriesUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}