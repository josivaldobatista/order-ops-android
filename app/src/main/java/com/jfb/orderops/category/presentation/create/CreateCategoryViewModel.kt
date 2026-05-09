package com.jfb.orderops.category.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.category.domain.usecase.CreateCategoryUseCase
import com.jfb.orderops.core.result.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateCategoryUiState()
    )

    val uiState: StateFlow<CreateCategoryUiState> =
        _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(name = value)
        }
    }

    fun create(
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result = createCategoryUseCase.execute(
                    name = _uiState.value.name
                )
            ) {

                is AppResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            success = true
                        )
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

                is AppResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}