package com.jfb.orderops.order.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.usecase.AddOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.CancelOrderUseCase
import com.jfb.orderops.order.domain.usecase.FinishOrderUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.MarkOrderAsReadyUseCase
import com.jfb.orderops.order.domain.usecase.RemoveOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.SendOrderToPreparationUseCase
import com.jfb.orderops.order.presentation.state.OrderDetailUiState
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val sendToPreparationUseCase: SendOrderToPreparationUseCase,
    private val markAsReadyUseCase: MarkOrderAsReadyUseCase,
    private val finishOrderUseCase: FinishOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val addOrderItemUseCase: AddOrderItemUseCase,
    private val removeOrderItemUseCase: RemoveOrderItemUseCase,
    private val listProductsUseCase: ListProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun loadAll() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val orderResult = getOrderByIdUseCase.execute(orderId)

            if (orderResult is AppResult.Error) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = orderResult.message
                    )
                }
                return@launch
            }

            val productsResult = listProductsUseCase.execute()

            when {
                orderResult is AppResult.Success && productsResult is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = orderResult.data,
                            products = productsResult.data,
                            errorMessage = null
                        )
                    }
                }

                productsResult is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = productsResult.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

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

    fun sendToPreparation() {
        updateOrder {
            sendToPreparationUseCase.execute(orderId)
        }
    }

    fun markAsReady() {
        updateOrder {
            markAsReadyUseCase.execute(orderId)
        }
    }

    fun finish() {
        updateOrder {
            finishOrderUseCase.execute(orderId)
        }
    }

    fun cancel() {
        updateOrder {
            cancelOrderUseCase.execute(orderId)
        }
    }

    fun addItem(productId: Long, quantity: Int) {
        updateOrder {
            addOrderItemUseCase.execute(
                orderId = orderId,
                productId = productId,
                quantity = quantity
            )
        }
    }

    fun removeItem(itemId: Long) {
        updateOrder {
            removeOrderItemUseCase.execute(
                orderId = orderId,
                itemId = itemId
            )
        }
    }

    private fun updateOrder(
        action: suspend () -> AppResult<Order>
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = action()) {
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