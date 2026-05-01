package com.jfb.orderops.order.domain.model

data class Order(
    val id: Long,
    val serviceTableId: Long,
    val status: OrderStatus,
    val totalAmount: Double,
    val items: List<OrderItem>
)

data class OrderItem(
    val id: Long,
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)

enum class OrderStatus {
    OPEN,
    IN_PREPARATION,
    READY,
    FINISHED,
    CANCELLED,
    UNKNOWN
}