package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.model.OrderStatus

@Composable
fun OrderItemsSection(
    order: Order,
    participants: List<OrderParticipant>,
    isLoading: Boolean,
    onRemoveItem: (Long) -> Unit,
    onAssignItemParticipant: (Long, Long?) -> Unit
) {
    val hasUnassignedItems = order.items.any {
        it.participantId == null
    }

    var expanded by remember(order.items) {
        mutableStateOf(hasUnassignedItems)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) {
                        "▲ Itens do pedido (${order.items.size})"
                    } else {
                        "▼ Itens do pedido (${order.items.size})"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                if (order.items.isEmpty()) {
                    Text(
                        text = "Nenhum item no pedido.",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    order.items.forEach { item ->
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

                                Spacer(Modifier.height(12.dp))

                                if (order.status.canEditItems()) {
                                    ItemParticipantDropdown(
                                        selectedParticipantId = item.participantId,
                                        participants = participants,
                                        isLoading = isLoading,
                                        onSelected = { participantId ->
                                            onAssignItemParticipant(item.id, participantId)
                                        }
                                    )
                                } else {
                                    Text(
                                        text = "Consumo: ${item.participantName ?: "Não atribuído"}",
                                        color = if (item.participantId == null) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    )
                                }

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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemParticipantDropdown(
    selectedParticipantId: Long?,
    participants: List<OrderParticipant>,
    isLoading: Boolean,
    onSelected: (Long?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedText = participants
        .firstOrNull { it.id == selectedParticipantId }
        ?.name
        ?: "Não atribuído"

    Text(
        text = "Consumo",
        style = MaterialTheme.typography.labelLarge
    )

    Spacer(Modifier.height(4.dp))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (!isLoading) {
                expanded = !expanded
            }
        }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            enabled = !isLoading,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Não atribuído") },
                onClick = {
                    expanded = false
                    onSelected(null)
                },
                enabled = !isLoading
            )

            participants.forEach { participant ->
                DropdownMenuItem(
                    text = { Text(participant.name) },
                    onClick = {
                        expanded = false
                        onSelected(participant.id)
                    },
                    enabled = !isLoading
                )
            }
        }
    }
}

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}