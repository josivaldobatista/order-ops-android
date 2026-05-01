package com.jfb.orderops.order.data.dto

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderItem
import com.jfb.orderops.order.domain.model.OrderStatus

fun OrderResponse.toDomain(): Order {
    return Order(
        id = id,
        serviceTableId = serviceTableId,
        status = runCatching {
            OrderStatus.valueOf(status)
        }.getOrDefault(OrderStatus.UNKNOWN),
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
        totalPrice = totalPrice
    )
}