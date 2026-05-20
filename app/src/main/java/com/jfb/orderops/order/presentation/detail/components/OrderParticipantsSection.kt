package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.core.ui.components.DashedBorderBox
import com.jfb.orderops.order.domain.model.OrderParticipant

@Composable
fun OrderParticipantsSection(
    participants: List<OrderParticipant>,
    newParticipantName: String,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onAddParticipant: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showParticipantsDialog by remember { mutableStateOf(false) }

    DashedBorderBox(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f),
        cornerRadius = 7.dp,
        strokeWidth = 1.dp,
        dashWidth = 8.dp,
        gapWidth = 3.dp,
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(5.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.34f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
                ),
                onClick = {
                    showParticipantsDialog = true
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_users),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = "Participantes",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(5.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
                ),
                onClick = {
                    showAddDialog = true
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_circle_plus),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = "Adicionar",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(6.dp))

    if (participants.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nenhum participante adicionado ainda.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
            )
        }
    } else {
        Text(
            text = "${participants.size} participantes cadastrados",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.58f)
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
            title = {
                Text("Novo participante")
            },
            text = {
                OutlinedTextField(
                    value = newParticipantName,
                    onValueChange = onNameChange,
                    label = {
                        Text("Nome")
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAddParticipant()
                        showAddDialog = false
                    },
                    enabled = !isLoading && newParticipantName.isNotBlank()
                ) {
                    Text(
                        text = "Salvar",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }

    if (showParticipantsDialog) {
        AlertDialog(
            onDismissRequest = {
                showParticipantsDialog = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
            title = {
                Text("Participantes")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (participants.isEmpty()) {
                        Text(
                            text = "Nenhum participante adicionado ainda.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        participants.forEach { participant ->
                            ParticipantDialogRow(name = participant.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showParticipantsDialog = false
                    }
                ) {
                    Text(
                        text = "Fechar",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}

@Composable
private fun ParticipantDialogRow(
    name: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_users),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}