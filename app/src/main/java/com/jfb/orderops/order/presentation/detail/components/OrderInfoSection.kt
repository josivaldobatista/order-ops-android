package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.core.ui.components.ComandexBadgeType
import com.jfb.orderops.core.ui.components.ComandexScreenHeader
import com.jfb.orderops.core.ui.components.ComandexStatusBadge
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderStatus
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderInfoSection(
    order: Order,
    isLoading: Boolean,
    onBack: () -> Unit
) {
    ComandexScreenHeader(
        title = "Pedido #${order.id}",
        subtitle = order.contextText(),
        onBack = onBack,
        enabled = !isLoading,
        trailing = {
            ComandexStatusBadge(
                text = order.status.toReadableText().uppercase(),
                type = order.status.toBadgeType()
            )
        }
    )

    Spacer(modifier = Modifier.height(14.dp))

    OrderSummaryCard(order = order)
}

@Composable
private fun OrderSummaryCard(
    order: Order
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surface.copy(alpha = 0.42f),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(alpha = 0.14f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Total do pedido",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = order.totalAmount.toCurrency(),
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    label = "Itens",
                    value = order.items.size.toString()
                )

                SummaryItem(
                    label = "Participantes",
                    value = "-"
                )

                SummaryItem(
                    label = "Mesa",
                    value = order.serviceTableId?.toString() ?: "-"
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String
) {
    val colors = MaterialTheme.colorScheme

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun Order.contextText(): String {
    return listOfNotNull(
        serviceTableId?.let { "Mesa $it" },
        fulfillmentType.toReadableText()
    ).joinToString(" • ")
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
        OrderFulfillmentType.DINE_IN -> "Atendimento local"
        OrderFulfillmentType.TAKEOUT -> "Retirada no balcão"
        OrderFulfillmentType.DELIVERY -> "Pedido para entrega"
        OrderFulfillmentType.UNKNOWN -> "Atendimento"
    }
}

private fun Double.toCurrency(): String {
    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(this)
}

private fun OrderStatus.toBadgeType(): ComandexBadgeType {
    return when (this) {
        OrderStatus.OPEN -> ComandexBadgeType.SUCCESS
        OrderStatus.IN_PREPARATION -> ComandexBadgeType.PRIMARY
        OrderStatus.READY -> ComandexBadgeType.WARNING
        OrderStatus.FINISHED -> ComandexBadgeType.NEUTRAL
        OrderStatus.CANCELLED -> ComandexBadgeType.ERROR
        OrderStatus.UNKNOWN -> ComandexBadgeType.NEUTRAL
    }
}