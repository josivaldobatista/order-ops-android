package com.jfb.orderops.receipt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.company.domain.usecase.GetCompanyByIdUseCase
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReceiptViewModel(
    private val orderId: Long,
    private val companyId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getCompanyByIdUseCase: GetCompanyByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptUiState())
    val uiState: StateFlow<ReceiptUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val orderResult = getOrderByIdUseCase.execute(orderId)
            val companyResult = getCompanyByIdUseCase.execute(companyId)

            if (orderResult is AppResult.Success && companyResult is AppResult.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        order = orderResult.data,
                        company = companyResult.data
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar comprovante."
                )
            }
        }
    }
}