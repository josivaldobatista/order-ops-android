package com.jfb.orderops.order.data.dto

import com.jfb.orderops.order.domain.model.OrderParticipant

data class OrderParticipantResponse(
    val id: Long,
    val name: String
)

fun OrderParticipantResponse.toDomain(): OrderParticipant {
    return OrderParticipant(
        id = id,
        name = name
    )
}