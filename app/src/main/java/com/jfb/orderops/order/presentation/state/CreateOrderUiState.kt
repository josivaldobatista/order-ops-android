package com.jfb.orderops.order.presentation.state

import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.product.domain.model.Product

data class CreateOrderUiState(
    val serviceTableId: Long = 0,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,
    val selectedProductId: Long? = null,
    val quantity: Int = 1,
    val items: List<CreateOrderItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CreateOrderItemUiState(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
) {
    val totalPrice: Double
        get() = quantity * unitPrice
}