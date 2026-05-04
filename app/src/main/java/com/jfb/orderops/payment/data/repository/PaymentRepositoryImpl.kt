package com.jfb.orderops.payment.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.payment.data.dto.PayOrderRequest
import com.jfb.orderops.payment.data.dto.toDomain
import com.jfb.orderops.payment.data.remote.PaymentApi
import com.jfb.orderops.payment.domain.model.Payment
import com.jfb.orderops.payment.domain.model.PaymentReport
import com.jfb.orderops.payment.domain.repository.PaymentRepository
import java.math.BigDecimal

class PaymentRepositoryImpl(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun payOrder(
        orderId: Long,
        method: String,
        amount: Double
    ): AppResult<Payment> {
        return try {
            val response = api.payOrder(
                orderId = orderId,
                request = PayOrderRequest(
                    method = method,
                    amount = BigDecimal.valueOf(amount)
                )
            )

            AppResult.Success(response.toDomain())

        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao pagar pedido",
                throwable = e
            )
        }
    }

    override suspend fun getReport(
        start: String,
        end: String
    ): AppResult<PaymentReport> {
        return try {
            val reportResponse = api.getReport(
                start = start,
                end = end
            )

            val ticketAverage = api.getTicketAverage(
                start = start,
                end = end
            )

            AppResult.Success(
                reportResponse.toDomain(ticketAverage)
            )
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar relatório.",
                throwable = e
            )
        }
    }
}