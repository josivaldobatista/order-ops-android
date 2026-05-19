package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderParticipant
import com.jfb.orderops.order.domain.model.OrderStatus
import java.text.NumberFormat
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
        var selectedParticipantFilter by remember {
            mutableStateOf<Long?>(Long.MIN_VALUE)
        }

        val filteredItems = when (selectedParticipantFilter) {
            Long.MIN_VALUE -> order.items

            null -> order.items.filter {
                it.participantId == null
            }

            else -> {
                val selectedParticipantName = participants
                    .firstOrNull { it.id == selectedParticipantFilter }
                    ?.name
                    ?.trim()
                    ?.lowercase()

                order.items.filter { item ->
                    val itemParticipantName = item.participantName
                        ?.trim()
                        ?.lowercase()

                    item.participantId == selectedParticipantFilter ||
                            itemParticipantName == selectedParticipantName
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Itens do pedido (${filteredItems.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            if (order.status.canEditItems()) {
                AttributionButton(
                    participants = participants,
                    selectedParticipantId = selectedParticipantFilter,
                    onSelected = {
                        selectedParticipantFilter = it
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        if (order.items.isEmpty()) {
            EmptyItemsCard()
            return@Column
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filteredItems.forEach { item ->
                OrderItemCompactCard(
                    productName = item.productName,
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    totalPrice = item.totalPrice,
                    selectedParticipantId = item.participantId,
                    selectedParticipantName = item.participantName,
                    participants = participants,
                    canEdit = order.status.canEditItems(),
                    isLoading = isLoading,
                    onAssignParticipant = { participantId ->
                        onAssignItemParticipant(item.id, participantId)
                    },
                    onRemove = {
                        onRemoveItem(item.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun AttributionButton(
    participants: List<OrderParticipant>,
    selectedParticipantId: Long?,
    onSelected: (Long?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText = when (selectedParticipantId) {

        Long.MIN_VALUE -> "Todos"

        null -> "Não atribuídos"

        else -> {
            participants.firstOrNull {
                it.id == selectedParticipantId
            }?.name ?: "Participante"
        }
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.42f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        ),
        modifier = Modifier.clickable {
            expanded = true
        }
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 7.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "👤",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = selectedText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                DropdownMenuItem(
                    text = {
                        Text("Todos")
                    },
                    onClick = {
                        expanded = false
                        onSelected(Long.MIN_VALUE)
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Não atribuídos")
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
private fun OrderItemCompactCard(
    productName: String,
    quantity: Int,
    unitPrice: Double,
    totalPrice: Double,
    selectedParticipantId: Long?,
    selectedParticipantName: String?,
    participants: List<OrderParticipant>,
    canEdit: Boolean,
    isLoading: Boolean,
    onAssignParticipant: (Long?) -> Unit,
    onRemove: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePlaceholder(productName)

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = productName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = totalPrice.toCurrency(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (canEdit) {
                        Spacer(Modifier.width(4.dp))

                        Box {
                            IconButton(
                                onClick = { menuExpanded = true },
                                enabled = !isLoading,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MoreVert,
                                    contentDescription = "Ações do item",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Remover item") },
                                    onClick = {
                                        menuExpanded = false
                                        onRemove()
                                    },
                                    enabled = !isLoading
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(3.dp))

                Text(
                    text = "Qtd $quantity • ${unitPrice.toCurrency()} cada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                ParticipantSelectorChip(
                    selectedParticipantId = selectedParticipantId,
                    selectedParticipantName = selectedParticipantName,
                    participants = participants,
                    isLoading = isLoading,
                    onSelected = onAssignParticipant
                )
            }
        }
    }
}

@Composable
private fun ProductImagePlaceholder(name: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
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
    var expanded by remember { mutableStateOf(false) }

    val participantName = selectedParticipantName ?: "Não atribuído"

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.10f)
        ),
        modifier = Modifier.clickable(enabled = !isLoading) {
            expanded = true
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "👤",
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = participantName,
                style = MaterialTheme.typography.labelSmall,
                color = if (selectedParticipantId != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Spacer(Modifier.width(2.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )

            DropdownMenu(
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
}

@Composable
private fun EmptyItemsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        )
    ) {
        Text(
            text = "Nenhum item no pedido.",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun Double.toCurrency(): String {
    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(this)
}

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}