package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.model.OrderStatus
import java.util.Locale

@Composable
fun OrderItemsSection(
    order: Order,
    participants: List<OrderParticipant>,
    isLoading: Boolean,
    onRemoveItem: (Long) -> Unit,
    onAssignItemParticipant: (Long, Long?) -> Unit
) {

    Column {

        Text(
            text = "Itens do pedido (${order.items.size})",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(14.dp))

        if (order.items.isEmpty()) {
            EmptyItemsCard()
            return@Column
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            order.items.forEach { item ->

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 12.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {

                            ProductImagePlaceholder()

                            Spacer(Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = item.productName,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(Modifier.height(4.dp))

                                        Text(
                                            text = "Qtd ${item.quantity} • ${item.unitPrice.toCurrency()} cada",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Text(
                                        text = item.totalPrice.toCurrency(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(Modifier.height(8.dp))

                                ParticipantSelectorChip(
                                    selectedParticipantId = item.participantId,
                                    selectedParticipantName = item.participantName,
                                    participants = participants,
                                    isLoading = isLoading,
                                    onSelected = { participantId ->
                                        onAssignItemParticipant(item.id, participantId)
                                    }
                                )

                                if (order.status.canEditItems()) {

                                    Spacer(Modifier.height(2.dp))

                                    TextButton(
                                        onClick = {
                                            onRemoveItem(item.id)
                                        },
                                        enabled = !isLoading,
                                        contentPadding = PaddingValues(0.dp)
                                    ) {

                                        Text(
                                            text = "Remover",
                                            color = MaterialTheme.colorScheme.primary
                                        )
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

@Composable
private fun ProductImagePlaceholder() {

    Surface(
        modifier = Modifier.size(58.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "🍔",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun ParticipantSelectorChip(
    selectedParticipantId: Long?,
    selectedParticipantName: String?,
    participants: List<OrderParticipant>,
    isLoading: Boolean,
    onSelected: (Long?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val participantName = selectedParticipantName ?: "Não atribuído"

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)
        ),
        modifier = Modifier.clickable(
            enabled = !isLoading
        ) {
            expanded = true
        }
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "👤",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = participantName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedParticipantId != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                }
            )

            Spacer(Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                DropdownMenuItem(
                    text = {
                        Text("Não atribuído")
                    },
                    onClick = {
                        expanded = false
                        onSelected(null)
                    }
                )

                participants.forEach { participant ->

                    DropdownMenuItem(
                        text = {
                            Text(participant.name)
                        },
                        onClick = {
                            expanded = false
                            onSelected(participant.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyItemsCard() {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f)
        )
    ) {

        Text(
            text = "Nenhum item no pedido.",
            modifier = Modifier.padding(18.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )
    }
}

private fun Double.toCurrency(): String {
    return "R$ ${String.format(Locale("pt", "BR"), "%.2f", this)}"
}

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}