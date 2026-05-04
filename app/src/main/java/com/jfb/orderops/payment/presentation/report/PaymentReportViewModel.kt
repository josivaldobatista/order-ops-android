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
import com.jfb.orderops.payment.domain.usecase.GetDailyPaymentReportUseCase
import com.jfb.orderops.payment.domain.model.DailyPaymentReport

class PaymentReportViewModel(
    private val useCase: GetPaymentReportUseCase,
    private val dailyUseCase: GetDailyPaymentReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentReportUiState())
    val uiState: StateFlow<PaymentReportUiState> = _uiState.asStateFlow()

    fun loadToday() {
        val today = LocalDate.now().toString()

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val reportResult = useCase.execute(today, today)
            val dailyResult = dailyUseCase.execute(today, today)

            if (reportResult is AppResult.Success && dailyResult is AppResult.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        report = reportResult.data,
                        daily = dailyResult.data
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar relatório."
                    )
                }
            }
        }
    }
}