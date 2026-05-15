package com.jfb.orderops.order.data.dto

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderItem
import com.jfb.orderops.order.domain.model.OrderStatus

fun OrderResponse.toDomain(): Order {
    return Order(
        id = id,
        serviceTableId = serviceTableId,
        fulfillmentType = fulfillmentType.toOrderFulfillmentType(),
        status = status.toOrderStatus(),
        totalAmount = totalAmount,
        items = items.map { it.toDomain() }
    )
}

fun OrderItemResponse.toDomain(): OrderItem {
    return OrderItem(
        id = id,
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity,
        totalPrice = totalPrice,
        participantId = participantId,
        participantName = participantName
    )
}

private fun String?.toOrderStatus(): OrderStatus {
    return runCatching {
        OrderStatus.valueOf(this ?: "")
    }.getOrDefault(OrderStatus.UNKNOWN)
}

private fun String?.toOrderFulfillmentType(): OrderFulfillmentType {
    return runCatching {
        OrderFulfillmentType.valueOf(this ?: "")
    }.getOrDefault(OrderFulfillmentType.UNKNOWN)
}