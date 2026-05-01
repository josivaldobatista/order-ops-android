package com.jfb.orderops.serviceTable.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase

class ServiceTablesViewModelFactory(
    private val listServiceTablesUseCase: ListServiceTablesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceTablesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceTablesViewModel(listServiceTablesUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}