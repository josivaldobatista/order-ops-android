package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
    when (status) {
        OrderStatus.OPEN -> {
            ActionButtonsRow(
                primaryText = "Enviar para preparo",
                secondaryText = "Cancelar pedido",
                isLoading = isLoading,
                onPrimaryClick = onSendToPreparation,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.IN_PREPARATION -> {
            ActionButtonsRow(
                primaryText = "Marcar como pronto",
                secondaryText = "Cancelar pedido",
                isLoading = isLoading,
                onPrimaryClick = onMarkAsReady,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.READY -> {
            ActionButtonsRow(
                primaryText = "Ir para pagamento",
                secondaryText = "Cancelar pedido",
                isLoading = isLoading,
                onPrimaryClick = onFinish,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.FINISHED -> FinalStatusMessage("Pedido finalizado.")
        OrderStatus.CANCELLED -> FinalStatusMessage("Pedido cancelado.")
        OrderStatus.UNKNOWN -> FinalStatusMessage("Status desconhecido.")
    }
}

@Composable
private fun ActionButtonsRow(
    primaryText: String,
    secondaryText: String,
    isLoading: Boolean,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Column {
        Text(
            text = "Ações do pedido",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPrimaryClick,
                enabled = !isLoading,
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = primaryText,
                    maxLines = 1
                )
            }

            OutlinedButton(
                onClick = onSecondaryClick,
                enabled = !isLoading,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                )
            ) {
                Text(
                    text = secondaryText,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun FinalStatusMessage(
    text: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}