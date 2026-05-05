package com.jfb.orderops.receipt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.company.domain.usecase.GetCompanyByIdUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase

class ReceiptViewModelFactory(
    private val orderId: Long,
    private val companyId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getCompanyByIdUseCase: GetCompanyByIdUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReceiptViewModel(
                orderId = orderId,
                companyId = companyId,
                getOrderByIdUseCase = getOrderByIdUseCase,
                getCompanyByIdUseCase = getCompanyByIdUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}