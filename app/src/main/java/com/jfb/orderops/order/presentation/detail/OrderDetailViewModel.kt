package com.jfb.orderops.order.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.presentation.state.OrderDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun loadOrder() {
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
}