package com.jfb.orderops.payment.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.payment.domain.model.PaymentMethod
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase
import com.jfb.orderops.payment.presentation.state.PaymentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel(
    orderId: Long,
    amount: Double,
    private val payOrderUseCase: PayOrderUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PaymentUiState(
            orderId = orderId,
            amount = amount
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun loadOrder() {
        val orderId = _uiState.value.orderId

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = getOrderByIdUseCase.execute(orderId)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = result.data,
                            amount = result.data.totalAmount,
                            errorMessage = null
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

    fun onMethodSelected(method: PaymentMethod) {
        _uiState.update {
            it.copy(
                selectedMethod = method,
                errorMessage = null
            )
        }
    }

    fun pay(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (
                val result = payOrderUseCase.execute(
                    orderId = state.orderId,
                    method = state.selectedMethod.name,
                    amount = state.amount
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }

                    onSuccess()
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