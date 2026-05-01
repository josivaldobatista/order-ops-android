package com.jfb.orderops.serviceTable.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.repository.ServiceTableRepository

class ListServiceTablesUseCase(
    private val repository: ServiceTableRepository
) {

    suspend fun execute(): AppResult<List<ServiceTable>> {
        return repository.list()
    }
}