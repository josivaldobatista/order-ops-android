package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.core.ui.components.DashboardMetricCard
import com.jfb.orderops.R

@Composable
fun DashboardHomeContent(
    tablesCount: Int,
    ordersCount: Int,
    productsCount: Int,
    onOpenTables: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenReports: () -> Unit,
    onOrderClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardMetricCard(
                title = "Mesas",
                value = tablesCount.toString(),
                subtitle = "Gerenciar",
                iconRes = R.drawable.ic_turntable,
                iconColor = Color(0xFFB94A2E),
                arrowColor = MaterialTheme.colorScheme.primary,
                onClick = onOpenTables,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Pedidos",
                value = ordersCount.toString(),
                subtitle = "Toque para acompanhar",
                iconRes = R.drawable.ic_receipt,
                iconColor = Color(0xFF1D4ED8),
                arrowColor = Color(0xFF3B82F6),
                onClick = onOpenOrders,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardMetricCard(
                title = "Produtos",
                value = productsCount.toString(),
                subtitle = "Cardápio",
                iconRes = R.drawable.ic_receipt_text,
                iconColor = Color(0xFF2A9D8F),
                arrowColor = Color(0xFF2A9D8F),
                onClick = onOpenProducts,
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Relatórios",
                value = "Ver",
                subtitle = "Desempenho",
                iconRes = R.drawable.ic_chart_column,
                iconColor = Color(0xFF6D3BBF),
                arrowColor = Color(0xFF9B5DE5),
                onClick = onOpenReports,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pedidos recentes",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ver todos",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        RecentOrderPreviewCard(
            orderId = 1254L,
            code = "#1254",
            table = "Mesa 04",
            status = "Em preparo",
            value = "R$ 87,50",
            time = "14:28",
            onClick = onOrderClick
        )

        Spacer(modifier = Modifier.height(8.dp))
        RecentOrderPreviewCard(
            orderId = 1253L,
            code = "#1253",
            table = "Mesa 02",
            status = "Aguardando",
            value = "R$ 65,00",
            time = "14:20",
            onClick = onOrderClick
        )

        Spacer(modifier = Modifier.height(8.dp))
        RecentOrderPreviewCard(
            orderId = 1252L,
            code = "#1252",
            table = "Mesa 07",
            status = "Pronto",
            value = "R$ 120,00",
            time = "14:15",
            onClick = onOrderClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}