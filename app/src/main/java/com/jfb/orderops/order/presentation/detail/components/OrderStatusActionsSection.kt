package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

    when (status) {

        OrderStatus.OPEN -> {

            ActionButtonsRow(
                primaryText = "Preparo",
                secondaryText = "Cancelar",
                isLoading = isLoading,
                onPrimaryClick = onSendToPreparation,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.IN_PREPARATION -> {

            ActionButtonsRow(
                primaryText = "Pronto",
                secondaryText = "Cancelar",
                isLoading = isLoading,
                onPrimaryClick = onMarkAsReady,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.READY -> {

            ActionButtonsRow(
                primaryText = "Pagamento",
                secondaryText = "Cancelar",
                isLoading = isLoading,
                onPrimaryClick = onFinish,
                onSecondaryClick = onCancel
            )
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

@Composable
private fun ActionButtonsRow(
    primaryText: String,
    secondaryText: String,
    isLoading: Boolean,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Button(
            onClick = onPrimaryClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        ) {
            Text(primaryText)
        }

        OutlinedButton(
            onClick = onSecondaryClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        ) {
            Text(secondaryText)
        }
    }
}