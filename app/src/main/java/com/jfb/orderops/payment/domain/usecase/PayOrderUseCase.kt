package com.jfb.orderops.payment.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.domain.model.Payment
import com.jfb.orderops.payment.domain.repository.PaymentRepository

class PayOrderUseCase(
    private val repository: PaymentRepository
) {

    suspend fun execute(
        orderId: Long,
        method: String,
        amount: Double
    ): AppResult<Payment> {

        if (orderId <= 0) {
            return AppResult.Error("Pedido inválido")
        }

        if (amount <= 0) {
            return AppResult.Error("Valor inválido")
        }

        return repository.payOrder(orderId, method, amount)
    }
}