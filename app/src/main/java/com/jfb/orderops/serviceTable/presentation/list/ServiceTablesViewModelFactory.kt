package com.jfb.orderops.serviceTable.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase
import com.jfb.orderops.serviceTable.domain.usecase.OccupyServiceTableUseCase
import com.jfb.orderops.serviceTable.domain.usecase.ReleaseServiceTableUseCase
import com.jfb.orderops.serviceTable.domain.usecase.ReserveServiceTableUseCase

class ServiceTablesViewModelFactory(
    private val listServiceTablesUseCase: ListServiceTablesUseCase,
    private val reserveServiceTableUseCase: ReserveServiceTableUseCase,
    private val occupyServiceTableUseCase: OccupyServiceTableUseCase,
    private val releaseServiceTableUseCase: ReleaseServiceTableUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceTablesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceTablesViewModel(
                listServiceTablesUseCase = listServiceTablesUseCase,
                reserveServiceTableUseCase = reserveServiceTableUseCase,
                occupyServiceTableUseCase = occupyServiceTableUseCase,
                releaseServiceTableUseCase = releaseServiceTableUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}