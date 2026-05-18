package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.core.ui.components.DashboardMetricCard
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors

@Composable
fun DashboardHomeContent(
    tablesCount: Int,
    ordersCount: Int,
    productsCount: Int,
    recentOrders: List<Order>,
    onOpenTables: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenReports: () -> Unit,
    onOrderClick: (Long) -> Unit,
    onOpenAllOrders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    Column(modifier = modifier) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardMetricCard(
                title = "Mesas",
                value = tablesCount.toString(),
                subtitle = "Gerenciar",
                iconRes = R.drawable.ic_turntable,
                iconColor = colors.primary,
                arrowColor = colors.primary,
                onClick = onOpenTables,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Pedidos",
                value = ordersCount.toString(),
                subtitle = "Acompanhar",
                iconRes = R.drawable.ic_receipt,
                iconColor = extraColors.warning,
                arrowColor = extraColors.warning,
                onClick = onOpenOrders,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardMetricCard(
                title = "Cardápio",
                value = productsCount.toString(),
                subtitle = "Itens",
                iconRes = R.drawable.ic_hand_platter,
                iconColor = extraColors.success,
                arrowColor = extraColors.success,
                onClick = onOpenProducts,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Relatórios",
                value = "Ver",
                subtitle = "Desempenho",
                iconRes = R.drawable.ic_chart_column,
                iconColor = colors.secondary,
                arrowColor = colors.secondary,
                onClick = onOpenReports,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pedidos recentes",
                color = colors.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ver todos",
                color = colors.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    onOpenAllOrders()
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (recentOrders.isEmpty()) {
            Text(
                text = "Nenhum pedido recente.",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            recentOrders.take(3).forEachIndexed { index, order ->
                RecentOrderPreviewCard(
                    orderId = order.id,
                    code = "#${order.id}",
                    table = order.displayLocation(),
                    status = order.status.displayLabel(),
                    value = order.totalAmount.toCurrencyText(),
                    time = "",
                    fulfillmentIconRes = order.fulfillmentIcon(),
                    fulfillmentColor = order.fulfillmentColor(),
                    statusColor = order.status.statusColor(),
                    onClick = onOrderClick
                )

                if (index < recentOrders.take(3).lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun Order.displayLocation(): String {
    return when {
        serviceTableId != null -> "Mesa $serviceTableId"
        fulfillmentType == OrderFulfillmentType.DELIVERY -> "Entrega"
        fulfillmentType == OrderFulfillmentType.TAKEOUT -> "Retirada"
        else -> fulfillmentType.label
    }
}

private fun OrderStatus.displayLabel(): String {
    return when (this) {
        OrderStatus.OPEN -> "Aberto"
        OrderStatus.IN_PREPARATION -> "Em preparo"
        OrderStatus.READY -> "Pronto"
        OrderStatus.FINISHED -> "Finalizado"
        OrderStatus.CANCELLED -> "Cancelado"
        OrderStatus.UNKNOWN -> "Desconhecido"
    }
}

private fun Double.toCurrencyText(): String {
    return "R$ %.2f".format(this).replace(".", ",")
}

private fun Order.fulfillmentIcon(): Int {
    return when (fulfillmentType) {
        OrderFulfillmentType.DINE_IN -> R.drawable.ic_utensils_crossed
        OrderFulfillmentType.TAKEOUT -> R.drawable.ic_basket
        OrderFulfillmentType.DELIVERY -> R.drawable.ic_motorbike
        OrderFulfillmentType.UNKNOWN -> R.drawable.ic_receipt
    }
}

@Composable
private fun Order.fulfillmentColor(): Color {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    return when (fulfillmentType) {
        OrderFulfillmentType.DINE_IN -> colors.primary
        OrderFulfillmentType.TAKEOUT -> extraColors.warning
        OrderFulfillmentType.DELIVERY -> extraColors.success
        OrderFulfillmentType.UNKNOWN -> colors.onSurfaceVariant
    }
}

@Composable
private fun OrderStatus.statusColor(): Color {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    return when (this) {
        OrderStatus.OPEN -> extraColors.warning
        OrderStatus.IN_PREPARATION -> extraColors.warning
        OrderStatus.READY -> extraColors.success
        OrderStatus.FINISHED -> colors.onSurfaceVariant
        OrderStatus.CANCELLED -> colors.error
        OrderStatus.UNKNOWN -> colors.onSurfaceVariant
    }
}