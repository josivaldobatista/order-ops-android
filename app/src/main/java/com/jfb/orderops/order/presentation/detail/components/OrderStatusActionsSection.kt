package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
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
                primaryText = "Enviar Preparo",
                primaryIcon = R.drawable.ic_chef_hat,
                secondaryText = "Cancelar",
                secondaryIcon = R.drawable.ic_circle_x,
                isLoading = isLoading,
                onPrimaryClick = onSendToPreparation,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.IN_PREPARATION -> {
            ActionButtonsRow(
                primaryText = "Pronto",
                primaryIcon = R.drawable.ic_chef_hat,
                secondaryText = "Cancelar",
                secondaryIcon = R.drawable.ic_circle_x,
                isLoading = isLoading,
                onPrimaryClick = onMarkAsReady,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.READY -> {
            ActionButtonsRow(
                primaryText = "Pagamento",
                primaryIcon = R.drawable.ic_chef_hat,
                secondaryText = "Cancelar",
                secondaryIcon = R.drawable.ic_circle_x,
                isLoading = isLoading,
                onPrimaryClick = onFinish,
                onSecondaryClick = onCancel
            )
        }

        OrderStatus.FINISHED -> Unit
        OrderStatus.CANCELLED -> Unit
        OrderStatus.UNKNOWN -> Unit
    }
}

@Composable
private fun ActionButtonsRow(
    primaryText: String,
    primaryIcon: Int,
    secondaryText: String,
    secondaryIcon: Int,
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
            modifier = Modifier
                .weight(1f)
                .height(38.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Icon(
                painter = painterResource(primaryIcon),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = primaryText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }

        OutlinedButton(
            onClick = onSecondaryClick,
            enabled = !isLoading,
            modifier = Modifier
                .weight(1f)
                .height(38.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
            ),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Icon(
                painter = painterResource(secondaryIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = secondaryText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
        }
    }
}