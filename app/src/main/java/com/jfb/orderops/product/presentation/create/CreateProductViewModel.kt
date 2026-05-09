package com.jfb.orderops.product.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.usecase.CreateProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val createProductUseCase: CreateProductUseCase,
    private val listCategoriesUseCase: ListCategoriesUseCase
) : ViewModel() {

    init {
        loadCategories()
    }

    fun onCategorySelected(categoryId: Long?) {
        _uiState.update {
            it.copy(selectedCategoryId = categoryId)
        }
    }

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

        val price = state.price.toBigDecimalOrNull()

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
                    price = price,
                    categoryId = state.selectedCategoryId,
                    active = state.active
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

    private fun loadCategories() {
        viewModelScope.launch {

            when (val result = listCategoriesUseCase.execute()) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(categories = result.data)
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message
                        )
                    }
                }

                is AppResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}