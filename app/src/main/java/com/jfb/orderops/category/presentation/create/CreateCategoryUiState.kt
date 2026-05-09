package com.jfb.orderops.category.presentation.create

data class CreateCategoryUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)