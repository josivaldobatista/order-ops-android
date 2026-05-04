package com.jfb.orderops.payment.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.payment.domain.usecase.GetPaymentReportUseCase

class PaymentReportViewModelFactory(
    private val useCase: GetPaymentReportUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentReportViewModel(useCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}