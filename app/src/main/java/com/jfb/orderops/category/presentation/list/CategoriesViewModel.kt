package com.jfb.orderops.category.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.result.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val listCategoriesUseCase: ListCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = listCategoriesUseCase.execute()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data,
                            isLoading = false
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

                is AppResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}