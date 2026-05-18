package com.jfb.orderops.serviceTable.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.repository.ServiceTableRepository

class OccupyServiceTableUseCase(
    private val repository: ServiceTableRepository
) {

    suspend fun execute(id: Long): AppResult<ServiceTable> {
        return repository.occupy(id)
    }
}