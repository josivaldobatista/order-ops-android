package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
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
    participantCount: Int,
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

    OrderSummaryCard(
        order = order,
        participantCount = participantCount
    )
}

@Composable
private fun OrderSummaryCard(
    order: Order,
    participantCount: Int
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(999.dp),
                    color = colors.primary.copy(alpha = 0.10f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colors.primary.copy(alpha = 0.45f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_receipt_text),
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Total do pedido",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = order.totalAmount.toCurrency(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryItem(
                    modifier = Modifier.weight(1f),
                    label = "Itens",
                    value = order.items.size.toString()
                )

                SummaryDivider()

                SummaryItem(
                    modifier = Modifier.weight(1f),
                    label = "Participantes",
                    value = participantCount.toString()
                )

                SummaryDivider()

                SummaryItem(
                    modifier = Modifier.weight(1.35f),
                    label = "Atendimento",
                    value = order.fulfillmentType.toShortReadableText(),
                    secondaryValue = order.serviceTableId?.let { "Mesa $it" }
                )
            }
        }
    }
}

private fun OrderFulfillmentType.toShortReadableText(): String {
    return when (this) {
        OrderFulfillmentType.DINE_IN -> "Comer no local"
        OrderFulfillmentType.TAKEOUT -> "Retirada"
        OrderFulfillmentType.DELIVERY -> "Entrega"
        OrderFulfillmentType.UNKNOWN -> "Atendimento"
    }
}

@Composable
private fun SummaryDivider() {
    Spacer(modifier = Modifier.width(5.dp))

    Box(
        modifier = Modifier
            .height(42.dp)
            .width(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
    )

    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    secondaryValue: String? = null
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        secondaryValue?.let {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                maxLines = 1
            )
        }
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