package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.OrderParticipantConsumptionPreview
import com.jfb.orderops.order.domain.model.ParticipantConsumptionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConsumptionPreviewBottomSheet(
    preview: OrderParticipantConsumptionPreview,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Consumo por participante",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Total do pedido: R$ ${"%.2f".format(preview.totalAmount)}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            if (preview.participants.isEmpty() && preview.unassignedItems.isEmpty()) {
                Text("Nenhum consumo registrado.")
            }

            preview.participants.forEach { participant ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = participant.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(4.dp))

                        Text("Total: R$ ${"%.2f".format(participant.totalAmount)}")

                        Spacer(Modifier.height(8.dp))

                        participant.items.forEach { item ->
                            ConsumptionItem(item)
                        }
                    }
                }
            }

            if (preview.hasUnassignedItems) {
                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Itens sem participante",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(4.dp))

                        Text("Total: R$ ${"%.2f".format(preview.unassignedTotalAmount)}")

                        Spacer(Modifier.height(8.dp))

                        preview.unassignedItems.forEach { item ->
                            ConsumptionItem(item)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fechar")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ConsumptionItem(
    item: ParticipantConsumptionItem
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = item.productName,
            style = MaterialTheme.typography.bodyLarge
        )

        Text("Quantidade: ${item.quantity}")
        Text("Subtotal: R$ ${"%.2f".format(item.totalPrice)}")
    }
}