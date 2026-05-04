package com.jfb.orderops.serviceTable.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.repository.ServiceTableRepository

class CreateServiceTableUseCase(
    private val repository: ServiceTableRepository
) {

    suspend fun execute(
        number: String,
        capacity: Int
    ): AppResult<ServiceTable> {
        if (number.isBlank()) {
            return AppResult.Error("Número da mesa é obrigatório.")
        }

        if (capacity <= 0) {
            return AppResult.Error("Capacidade deve ser maior que zero.")
        }

        return repository.create(
            number = number.trim(),
            capacity = capacity
        )
    }
}