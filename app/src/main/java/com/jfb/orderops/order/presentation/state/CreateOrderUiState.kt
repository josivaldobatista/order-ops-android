package com.jfb.orderops.order.presentation.state

import com.jfb.orderops.product.domain.model.Product

data class CreateOrderUiState(
    val serviceTableId: Long = 0,
    val products: List<Product> = emptyList(),

    val selectedProductId: Long? = null,
    val selectedQuantity: Int = 1,

    val addedItems: Map<Long, Int> = emptyMap(),

    val isLoading: Boolean = false,
    val isCreatingOrder: Boolean = false,
    val errorMessage: String? = null
)