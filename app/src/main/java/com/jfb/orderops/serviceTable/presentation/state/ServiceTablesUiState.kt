package com.jfb.orderops.serviceTable.presentation.state

import com.jfb.orderops.serviceTable.domain.model.ServiceTable

data class ServiceTablesUiState(
    val isLoading: Boolean = false,
    val serviceTables: List<ServiceTable> = emptyList(),
    val errorMessage: String? = null
)