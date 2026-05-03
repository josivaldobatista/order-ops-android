package com.jfb.orderops.product.presentation.create

data class CreateProductUiState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)