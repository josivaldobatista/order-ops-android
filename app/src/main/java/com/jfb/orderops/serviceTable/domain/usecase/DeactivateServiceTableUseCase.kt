package com.jfb.orderops.serviceTable.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.repository.ServiceTableRepository

class DeactivateServiceTableUseCase(
    private val repository: ServiceTableRepository
) {

    suspend fun execute(id: Long): AppResult<Unit> {
        return repository.deactivate(id)
    }
}