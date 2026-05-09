package com.jfb.orderops.category.presentation.list

import com.jfb.orderops.category.domain.model.Category

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val selectedCategory: Category? = null,
    val editName: String = "",
    val isEditDialogVisible: Boolean = false
)