package com.jfb.orderops.order.data.dto

import com.jfb.orderops.order.domain.model.OrderParticipantConsumptionPreview
import com.jfb.orderops.order.domain.model.ParticipantConsumption
import com.jfb.orderops.order.domain.model.ParticipantConsumptionItem

fun OrderParticipantConsumptionPreviewResponse.toDomain():
        OrderParticipantConsumptionPreview {

    return OrderParticipantConsumptionPreview(
        orderId = orderId,
        totalAmount = totalAmount,
        participantsTotalAmount = participantsTotalAmount,
        participants = participants.map { it.toDomain() },
        unassignedTotalAmount = unassignedTotalAmount,
        hasUnassignedItems = hasUnassignedItems,
        unassignedItems = unassignedItems.map { it.toDomain() }
    )
}

fun ParticipantConsumptionResponse.toDomain():
        ParticipantConsumption {

    return ParticipantConsumption(
        id = id,
        name = name,
        totalAmount = totalAmount,
        items = items.map { it.toDomain() }
    )
}

fun ParticipantConsumptionItemResponse.toDomain():
        ParticipantConsumptionItem {

    return ParticipantConsumptionItem(
        orderItemId = orderItemId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice,
        totalPrice = totalPrice
    )
}