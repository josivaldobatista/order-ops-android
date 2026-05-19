package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.OrderParticipant

@Composable
fun OrderParticipantsSection(
    participants: List<OrderParticipant>,
    newParticipantName: String,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onAddParticipant: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    Column {

        Text(
            text = "Participantes",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.58f),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)
            ),
            onClick = {
                showDialog = true
            }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "⊕",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Adicionar participante",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        if (participants.isEmpty()) {

            Text(
                text = "Nenhum participante adicionado ainda.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f),
                modifier = Modifier.fillMaxWidth()
            )

        } else {

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                participants.forEach { participant ->

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(participant.name)
                        },
                        leadingIcon = {
                            Text("👤")
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
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
                        showDialog = false
                    },
                    enabled = !isLoading &&
                            newParticipantName.isNotBlank()
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}