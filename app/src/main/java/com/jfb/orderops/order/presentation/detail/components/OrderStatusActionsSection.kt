package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.OrderStatus

@Composable
fun OrderStatusActionsSection(
    status: OrderStatus,
    isLoading: Boolean,
    onSendToPreparation: () -> Unit,
    onMarkAsReady: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (status) {
            OrderStatus.OPEN -> {
                Button(
                    onClick = onSendToPreparation,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar para preparo")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.IN_PREPARATION -> {
                Button(
                    onClick = onMarkAsReady,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como pronto")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.READY -> {
                Button(
                    onClick = onFinish,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar pedido")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.FINISHED -> {
                Text(
                    text = "Pedido finalizado.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            OrderStatus.CANCELLED -> {
                Text(
                    text = "Pedido cancelado.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            OrderStatus.UNKNOWN -> {
                Text(
                    text = "Status desconhecido.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}