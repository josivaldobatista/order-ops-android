package com.jfb.orderops.product.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.usecase.CreateProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProductUiState())
    val uiState: StateFlow<CreateProductUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onPriceChange(value: String) {
        _uiState.update { it.copy(price = value) }
    }

    fun create(onSuccess: () -> Unit) {
        val state = _uiState.value

        val price = state.price.toDoubleOrNull()

        if (price == null) {
            _uiState.update {
                it.copy(errorMessage = "Preço inválido.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (
                val result = createProductUseCase.execute(
                    name = state.name,
                    description = state.description,
                    price = price
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