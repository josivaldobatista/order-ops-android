package com.jfb.orderops.payment.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.domain.usecase.GetPaymentReportUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class PaymentReportViewModel(
    private val useCase: GetPaymentReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentReportUiState())
    val uiState: StateFlow<PaymentReportUiState> = _uiState.asStateFlow()

    fun loadToday() {
        val today = LocalDate.now().toString()

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = useCase.execute(today, today)) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            report = result.data
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }
}