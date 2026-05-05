package com.jfb.orderops.receipt.presentation

import com.jfb.orderops.company.domain.model.Company
import com.jfb.orderops.order.domain.model.Order

data class ReceiptUiState(
    val order: Order? = null,
    val company: Company? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)