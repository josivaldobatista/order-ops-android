package com.jfb.orderops.serviceTable.presentation.create

data class CreateServiceTableUiState(
    val number: String = "",
    val capacity: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)