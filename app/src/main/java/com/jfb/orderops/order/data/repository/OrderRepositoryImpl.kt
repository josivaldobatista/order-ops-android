package com.jfb.orderops.order.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.data.dto.CreateOrderItemRequest
import com.jfb.orderops.order.data.dto.CreateOrderRequest
import com.jfb.orderops.order.data.dto.toDomain
import com.jfb.orderops.order.data.remote.OrderApi
import com.jfb.orderops.order.domain.model.CreateOrderItem
import com.jfb.orderops.order.domain.model.Order
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
        serviceTableId: Long,
        items: List<CreateOrderItem>
    ): AppResult<Order> {
        return try {
            val request = CreateOrderRequest(
                serviceTableId = serviceTableId,
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
}