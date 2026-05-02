package com.jfb.orderops.order.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.CreateOrderItem
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.order.presentation.state.CreateOrderUiState
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateOrderViewModel(
    private val serviceTableId: Long,
    private val listProductsUseCase: ListProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateOrderUiState(serviceTableId = serviceTableId)
    )
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = listProductsUseCase.execute()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = result.data,
                            selectedProductId = result.data.firstOrNull()?.id
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

    fun onProductSelected(productId: Long) {
        _uiState.update {
            it.copy(
                selectedProductId = productId,
                errorMessage = null
            )
        }
    }

    fun increaseQuantity() {
        _uiState.update {
            it.copy(selectedQuantity = it.selectedQuantity + 1)
        }
    }

    fun decreaseQuantity() {
        _uiState.update {
            it.copy(
                selectedQuantity = maxOf(1, it.selectedQuantity - 1)
            )
        }
    }

    fun addSelectedProduct() {
        val state = _uiState.value
        val productId = state.selectedProductId

        if (productId == null) {
            _uiState.update {
                it.copy(errorMessage = "Selecione um produto.")
            }
            return
        }

        val currentQuantity = state.addedItems[productId] ?: 0
        val newQuantity = currentQuantity + state.selectedQuantity

        _uiState.update {
            it.copy(
                addedItems = it.addedItems + (productId to newQuantity),
                selectedQuantity = 1,
                errorMessage = null
            )
        }
    }

    fun removeProduct(productId: Long) {
        _uiState.update {
            it.copy(
                addedItems = it.addedItems - productId
            )
        }
    }

    fun createOrder(
        onSuccess: (Long) -> Unit
    ) {
        val state = _uiState.value

        val items = state.addedItems.map { (productId, quantity) ->
            CreateOrderItem(
                productId = productId,
                quantity = quantity
            )
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isCreatingOrder = true, errorMessage = null)
            }

            when (
                val result = createOrderUseCase.execute(
                    serviceTableId = state.serviceTableId,
                    items = items
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(isCreatingOrder = false)
                    }

                    onSuccess(result.data.id)
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreatingOrder = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }
}