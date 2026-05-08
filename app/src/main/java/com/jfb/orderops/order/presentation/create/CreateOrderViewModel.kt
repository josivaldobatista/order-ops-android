package com.jfb.orderops.order.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.order.presentation.state.CreateOrderItemUiState
import com.jfb.orderops.order.presentation.state.CreateOrderUiState
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateOrderViewModel(
    serviceTableId: Long,
    private val listProductsUseCase: ListProductsUseCase,
    private val listCategoriesUseCase: ListCategoriesUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateOrderUiState(serviceTableId = serviceTableId)
    )
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val productsResult = listProductsUseCase.execute()
            val categoriesResult = listCategoriesUseCase.execute()

            if (productsResult is AppResult.Success && categoriesResult is AppResult.Success) {
                val categories = categoriesResult.data
                val selectedCategoryId = categories.firstOrNull()?.id

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = productsResult.data,
                        categories = categories,
                        selectedCategoryId = selectedCategoryId,
                        selectedProductId = productsResult.data
                            .firstOrNull { product -> product.categoryId == selectedCategoryId }
                            ?.id
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar produtos e categorias."
                )
            }
        }
    }

    fun onCategorySelected(categoryId: Long?) {
        val products = _uiState.value.products

        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId,
                selectedProductId = products
                    .firstOrNull { product ->
                        categoryId == null || product.categoryId == categoryId
                    }
                    ?.id
            )
        }
    }

    fun onProductSelected(productId: Long) {
        _uiState.update {
            it.copy(selectedProductId = productId)
        }
    }

    fun increaseQuantity() {
        _uiState.update {
            it.copy(quantity = it.quantity + 1)
        }
    }

    fun decreaseQuantity() {
        _uiState.update {
            it.copy(quantity = maxOf(1, it.quantity - 1))
        }
    }

    fun addSelectedProduct() {
        val state = _uiState.value
        val product = state.products.firstOrNull { it.id == state.selectedProductId }

        if (product == null) {
            _uiState.update {
                it.copy(errorMessage = "Selecione um produto.")
            }
            return
        }

        val existingItem = state.items.firstOrNull { it.productId == product.id }

        val updatedItems = if (existingItem != null) {
            state.items.map {
                if (it.productId == product.id) {
                    it.copy(quantity = it.quantity + state.quantity)
                } else {
                    it
                }
            }
        } else {
            state.items + product.toCreateOrderItemUiState(state.quantity)
        }

        _uiState.update {
            it.copy(
                items = updatedItems,
                quantity = 1,
                errorMessage = null
            )
        }
    }

    fun removeProduct(productId: Long) {
        _uiState.update {
            it.copy(
                items = it.items.filterNot { item -> item.productId == productId }
            )
        }
    }

    fun createOrder(onSuccess: (Long) -> Unit) {
        val state = _uiState.value

        if (state.items.isEmpty()) {
            _uiState.update {
                it.copy(errorMessage = "Adicione pelo menos um produto.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (
                val result = createOrderUseCase.execute(
                    serviceTableId = state.serviceTableId,
                    items = state.items.map {
                        com.jfb.orderops.order.domain.model.CreateOrderItem(
                            productId = it.productId,
                            quantity = it.quantity
                        )
                    }
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }

                    onSuccess(result.data.id)
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

private fun Product.toCreateOrderItemUiState(quantity: Int): CreateOrderItemUiState {
    return CreateOrderItemUiState(
        productId = id,
        productName = name,
        quantity = quantity,
        unitPrice = price
    )
}