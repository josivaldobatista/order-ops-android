package com.jfb.orderops.serviceTable.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.serviceTable.domain.usecase.CreateServiceTableUseCase

class CreateServiceTableViewModelFactory(
    private val createServiceTableUseCase: CreateServiceTableUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateServiceTableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateServiceTableViewModel(createServiceTableUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}