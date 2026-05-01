package com.jfb.orderops.serviceTable.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase
import com.jfb.orderops.serviceTable.presentation.state.ServiceTablesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceTablesViewModel(
    private val listServiceTablesUseCase: ListServiceTablesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceTablesUiState())
    val uiState: StateFlow<ServiceTablesUiState> = _uiState.asStateFlow()

    fun loadServiceTables() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = listServiceTablesUseCase.execute()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            serviceTables = result.data,
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