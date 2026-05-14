package com.jfb.orderops.order.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewResponse
import com.jfb.orderops.order.domain.model.CreateOrderItem
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderStatus

interface OrderRepository {

    suspend fun list(status: OrderStatus? = null): AppResult<List<Order>>

    suspend fun create(
        serviceTableId: Long?,
        fulfillmentType: OrderFulfillmentType,
        items: List<CreateOrderItem>
    ): AppResult<Order>

    suspend fun getById(id: Long): AppResult<Order>

    suspend fun sendToPreparation(id: Long): AppResult<Order>

    suspend fun markAsReady(id: Long): AppResult<Order>

    suspend fun finish(id: Long): AppResult<Order>

    suspend fun cancel(id: Long): AppResult<Order>

    suspend fun addItem(
        orderId: Long,
        productId: Long,
        quantity: Int
    ): AppResult<Order>

    suspend fun removeItem(
        orderId: Long,
        itemId: Long
    ): AppResult<Order>

    suspend fun previewPaymentSplit(
        orderId: Long,
        numberOfPeople: Int
    ): AppResult<PaymentSplitPreviewResponse>
}