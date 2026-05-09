package com.jfb.orderops.product.presentation.create

import com.jfb.orderops.category.domain.model.Category

data class CreateProductUiState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val active: Boolean = true,

    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,

    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)