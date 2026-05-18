package com.jfb.orderops.serviceTable.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase
import com.jfb.orderops.serviceTable.domain.usecase.OccupyServiceTableUseCase
import com.jfb.orderops.serviceTable.domain.usecase.ReleaseServiceTableUseCase
import com.jfb.orderops.serviceTable.domain.usecase.ReserveServiceTableUseCase
import com.jfb.orderops.serviceTable.presentation.state.ServiceTablesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceTablesViewModel(
    private val listServiceTablesUseCase: ListServiceTablesUseCase,
    private val reserveServiceTableUseCase: ReserveServiceTableUseCase,
    private val occupyServiceTableUseCase: OccupyServiceTableUseCase,
    private val releaseServiceTableUseCase: ReleaseServiceTableUseCase
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

    fun reserveTable(tableId: Long) {
        executeTableAction(
            action = { reserveServiceTableUseCase.execute(tableId) }
        )
    }

    fun occupyTable(tableId: Long) {
        executeTableAction(
            action = { occupyServiceTableUseCase.execute(tableId) }
        )
    }

    fun releaseTable(tableId: Long) {
        executeTableAction(
            action = { releaseServiceTableUseCase.execute(tableId) }
        )
    }

    private fun executeTableAction(
        action: suspend () -> AppResult<ServiceTable>
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = action()) {
                is AppResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            serviceTables = currentState.serviceTables.map { table ->
                                if (table.id == result.data.id) result.data else table
                            },
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