package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.OrderParticipantConsumptionPreview
import com.jfb.orderops.order.domain.model.ParticipantConsumptionItem

@Composable
fun OrderConsumptionPreviewSection(
    preview: OrderParticipantConsumptionPreview
) {

    Spacer(Modifier.height(24.dp))

    Text(
        text = "Consumo por participante",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(Modifier.height(8.dp))

    if (
        preview.participants.isEmpty() &&
        preview.unassignedItems.isEmpty()
    ) {

        Text(
            text = "Nenhum consumo registrado.",
            color = MaterialTheme.colorScheme.onBackground
        )

        return
    }

    preview.participants.forEach { participant ->

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
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = participant.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Total: R$ ${"%.2f".format(participant.totalAmount)}"
                )

                Spacer(Modifier.height(12.dp))

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

                Text(
                    text = "Total: R$ ${"%.2f".format(preview.unassignedTotalAmount)}"
                )

                Spacer(Modifier.height(12.dp))

                preview.unassignedItems.forEach { item ->
                    ConsumptionItem(item)
                }
            }
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

        Text(
            text = "Quantidade: ${item.quantity}"
        )

        Text(
            text = "Subtotal: R$ ${"%.2f".format(item.totalPrice)}"
        )
    }
}