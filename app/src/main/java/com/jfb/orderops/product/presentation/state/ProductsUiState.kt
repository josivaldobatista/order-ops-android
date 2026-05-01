package com.jfb.orderops.product.presentation.state

import com.jfb.orderops.product.domain.model.Product

data class ProductsUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null
)