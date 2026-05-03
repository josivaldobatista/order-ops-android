package com.jfb.orderops.order.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.presentation.state.OrdersUiState

@Composable
fun OrdersScreen(
    uiState: OrdersUiState,
    onRefresh: () -> Unit,
    onStatusSelected: (OrderStatus?) -> Unit,
    onOrderClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pedidos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        StatusFilter(
            selectedStatus = uiState.selectedStatus,
            onStatusSelected = onStatusSelected
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onRefresh,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar")
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.orders) { order ->
                OrderCard(
                    order = order,
                    onClick = { onOrderClick(order.id) }
                )
            }
        }
    }
}

@Composable
private fun StatusFilter(
    selectedStatus: OrderStatus?,
    onStatusSelected: (OrderStatus?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = { Text("Todos") }
            )

            FilterChip(
                selected = selectedStatus == OrderStatus.OPEN,
                onClick = { onStatusSelected(OrderStatus.OPEN) },
                label = { Text("Abertos") }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedStatus == OrderStatus.IN_PREPARATION,
                onClick = { onStatusSelected(OrderStatus.IN_PREPARATION) },
                label = { Text("Preparo") }
            )

            FilterChip(
                selected = selectedStatus == OrderStatus.READY,
                onClick = { onStatusSelected(OrderStatus.READY) },
                label = { Text("Prontos") }
            )

            FilterChip(
                selected = selectedStatus == OrderStatus.FINISHED,
                onClick = { onStatusSelected(OrderStatus.FINISHED) },
                label = { Text("Finalizados") }
            )
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Pedido #${order.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text("Mesa: ${order.serviceTableId}")
            Text("Status: ${order.status.toReadableText()}")
            Text("Total: R$ ${"%.2f".format(order.totalAmount)}")

            if (order.items.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Itens:",
                    style = MaterialTheme.typography.labelLarge
                )

                order.items.forEach { item ->
                    Text("- ${item.quantity}x ${item.productName}")
                }
            }
        }
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