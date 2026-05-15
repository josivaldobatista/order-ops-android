package com.jfb.orderops.order.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.repository.OrderRepository

class CreateOrderParticipantUseCase(
    private val repository: OrderRepository
) {

    suspend fun execute(
        orderId: Long,
        name: String
    ): AppResult<OrderParticipant> {
        val normalizedName = name.trim()

        if (normalizedName.isBlank()) {
            return AppResult.Error("Informe o nome do participante.")
        }

        if (normalizedName.length > 80) {
            return AppResult.Error("O nome do participante deve ter no máximo 80 caracteres.")
        }

        return repository.createParticipant(
            orderId = orderId,
            name = normalizedName
        )
    }
}