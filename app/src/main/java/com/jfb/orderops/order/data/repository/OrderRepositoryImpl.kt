package com.jfb.orderops.order.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.data.dto.AddOrderItemRequest
import com.jfb.orderops.order.data.dto.CreateOrderItemRequest
import com.jfb.orderops.order.data.dto.CreateOrderParticipantRequest
import com.jfb.orderops.order.data.dto.CreateOrderRequest
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewRequest
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewResponse
import com.jfb.orderops.order.data.dto.toDomain
import com.jfb.orderops.order.data.remote.OrderApi
import com.jfb.orderops.order.domain.model.CreateOrderItem
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.domain.repository.OrderRepository
import retrofit2.HttpException

class OrderRepositoryImpl(
    private val api: OrderApi
) : OrderRepository {

    override suspend fun list(status: OrderStatus?): AppResult<List<Order>> {
        return try {
            val orders = api.list(
                status = status?.takeIf { it != OrderStatus.UNKNOWN }?.name
            ).map { it.toDomain() }

            AppResult.Success(orders)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar pedidos.",
                throwable = e
            )
        }
    }

    override suspend fun create(
        serviceTableId: Long?,
        fulfillmentType: OrderFulfillmentType,
        items: List<CreateOrderItem>
    ): AppResult<Order> {
        return try {
            val request = CreateOrderRequest(
                serviceTableId = serviceTableId,
                fulfillmentType = fulfillmentType.name,
                items = items.map {
                    CreateOrderItemRequest(
                        productId = it.productId,
                        quantity = it.quantity
                    )
                }
            )

            val order = api.create(request).toDomain()

            AppResult.Success(order)
        } catch (e: HttpException) {
            AppResult.Error(
                message = "Erro HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}",
                throwable = e
            )
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao criar pedido.",
                throwable = e
            )
        }
    }

    override suspend fun getById(id: Long): AppResult<Order> {
        return try {
            val order = api.getById(id).toDomain()

            AppResult.Success(order)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar pedido.",
                throwable = e
            )
        }
    }

    override suspend fun sendToPreparation(id: Long): AppResult<Order> {
        return updateStatus {
            api.sendToPreparation(id).toDomain()
        }
    }

    override suspend fun markAsReady(id: Long): AppResult<Order> {
        return updateStatus {
            api.markAsReady(id).toDomain()
        }
    }

    override suspend fun finish(id: Long): AppResult<Order> {
        return updateStatus {
            api.finish(id).toDomain()
        }
    }

    override suspend fun cancel(id: Long): AppResult<Order> {
        return updateStatus {
            api.cancel(id).toDomain()
        }
    }

    private suspend fun updateStatus(
        action: suspend () -> Order
    ): AppResult<Order> {
        return try {
            AppResult.Success(action())
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao atualizar status do pedido.",
                throwable = e
            )
        }
    }

    override suspend fun addItem(
        orderId: Long,
        productId: Long,
        quantity: Int
    ): AppResult<Order> {
        return try {
            val order = api.addItem(
                orderId = orderId,
                request = AddOrderItemRequest(
                    productId = productId,
                    quantity = quantity
                )
            ).toDomain()

            AppResult.Success(order)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao adicionar item.",
                throwable = e
            )
        }
    }

    override suspend fun removeItem(
        orderId: Long,
        itemId: Long
    ): AppResult<Order> {
        return try {
            val order = api.removeItem(
                orderId = orderId,
                itemId = itemId
            ).toDomain()

            AppResult.Success(order)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao remover item.",
                throwable = e
            )
        }
    }

    override suspend fun previewPaymentSplit(
        orderId: Long,
        numberOfPeople: Int
    ): AppResult<PaymentSplitPreviewResponse> {
        return try {
            val preview = api.previewPaymentSplit(
                orderId = orderId,
                request = PaymentSplitPreviewRequest(
                    mode = "EQUAL",
                    numberOfPeople = numberOfPeople
                )
            )

            AppResult.Success(preview)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao calcular divisão do pagamento.",
                throwable = e
            )
        }
    }

    override suspend fun listParticipants(
        orderId: Long
    ): AppResult<List<OrderParticipant>> {
        return try {
            val participants = api.listParticipants(orderId)
                .map { it.toDomain() }

            AppResult.Success(participants)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar participantes.",
                throwable = e
            )
        }
    }

    override suspend fun createParticipant(
        orderId: Long,
        name: String
    ): AppResult<OrderParticipant> {
        return try {
            val participant = api.createParticipant(
                orderId = orderId,
                request = CreateOrderParticipantRequest(name = name)
            ).toDomain()

            AppResult.Success(participant)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao criar participante.",
                throwable = e
            )
        }
    }
}