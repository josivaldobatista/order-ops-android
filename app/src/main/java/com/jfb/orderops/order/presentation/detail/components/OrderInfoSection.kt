package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderStatus

@Composable
fun OrderInfoSection(
    order: Order
) {

    Text(
        text = "Pedido #${order.id}",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(Modifier.height(16.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Atendimento: ${order.fulfillmentType.toReadableText()}",
                color = order.fulfillmentType.toColor()
            )

            Spacer(Modifier.height(4.dp))

            if (order.serviceTableId != null) {
                Text("Mesa: ${order.serviceTableId}")
            }

            Text(
                text = "Status: ${order.status.toReadableText()}",
                color = order.status.toColor()
            )

            Text("Total: R$ ${"%.2f".format(order.totalAmount)}")
        }
    }
}

private fun OrderStatus.toColor(): Color {
    return when (this) {
        OrderStatus.OPEN -> Color(0xFF4CAF50)
        OrderStatus.IN_PREPARATION -> Color(0xFFFFC107)
        OrderStatus.READY -> Color(0xFF2196F3)
        OrderStatus.FINISHED -> Color(0xFF9E9E9E)
        OrderStatus.CANCELLED -> Color(0xFFF44336)
        OrderStatus.UNKNOWN -> Color.DarkGray
    }
}

private fun OrderStatus.toReadableText(): String {
    return when (this) {
        OrderStatus.OPEN -> "Aberto"
        OrderStatus.IN_PREPARATION -> "Em preparo"
        OrderStatus.READY -> "Pronto"
        OrderStatus.FINISHED -> "Finalizado"
        OrderStatus.CANCELLED -> "Cancelado"
        OrderStatus.UNKNOWN -> "Desconhecido"
    }
}

private fun OrderFulfillmentType.toReadableText(): String {
    return when (this) {
        OrderFulfillmentType.DINE_IN -> "Comer no local"
        OrderFulfillmentType.TAKEOUT -> "Retirada"
        OrderFulfillmentType.DELIVERY -> "Entrega"
        OrderFulfillmentType.UNKNOWN -> "Desconhecido"
    }
}

private fun OrderFulfillmentType.toColor(): Color {
    return when (this) {
        OrderFulfillmentType.DINE_IN -> Color(0xFF4CAF50)
        OrderFulfillmentType.TAKEOUT -> Color(0xFFFF9800)
        OrderFulfillmentType.DELIVERY -> Color(0xFF2196F3)
        OrderFulfillmentType.UNKNOWN -> Color.DarkGray
    }
}