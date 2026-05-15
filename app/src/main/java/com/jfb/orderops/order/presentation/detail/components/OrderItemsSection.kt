package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus

@Composable
fun OrderItemsSection(
    order: Order,
    isLoading: Boolean,
    onRemoveItem: (Long) -> Unit
) {

    Text(
        text = "Itens",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(Modifier.height(8.dp))

    if (order.items.isEmpty()) {

        Text(
            text = "Nenhum item no pedido.",
            color = MaterialTheme.colorScheme.onBackground
        )

    } else {

        order.items.forEach { item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(4.dp))

                    Text("Quantidade: ${item.quantity}")
                    Text("Unitário: R$ ${"%.2f".format(item.unitPrice)}")
                    Text("Subtotal: R$ ${"%.2f".format(item.totalPrice)}")

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Consumo: ${item.participantName ?: "Não atribuído"}",
                        color = if (item.participantId == null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )

                    if (order.status.canEditItems()) {
                        Spacer(Modifier.height(8.dp))

                        TextButton(
                            onClick = { onRemoveItem(item.id) },
                            enabled = !isLoading
                        ) {
                            Text("Remover")
                        }
                    }
                }
            }
        }
    }
}

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}