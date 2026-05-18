package com.jfb.orderops.serviceTable.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.usecase.CreateServiceTableUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateServiceTableViewModel(
    private val createServiceTableUseCase: CreateServiceTableUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateServiceTableUiState())
    val uiState: StateFlow<CreateServiceTableUiState> = _uiState.asStateFlow()

    fun onNumberChange(value: String) {
        _uiState.update { it.copy(number = value) }
    }

    fun onCapacityChange(value: String) {
        _uiState.update { it.copy(capacity = value) }
    }

    fun create(onSuccess: () -> Unit) {

        val state = _uiState.value

        val number = state.number.trim()
        val capacity = state.capacity.toIntOrNull()

        when {

            number.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Informe o número da mesa.")
                }
                return
            }

            capacity == null -> {
                _uiState.update {
                    it.copy(errorMessage = "Capacidade inválida.")
                }
                return
            }

            capacity <= 0 -> {
                _uiState.update {
                    it.copy(errorMessage = "A capacidade deve ser maior que zero.")
                }
                return
            }
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result = createServiceTableUseCase.execute(
                    number = number,
                    capacity = capacity
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