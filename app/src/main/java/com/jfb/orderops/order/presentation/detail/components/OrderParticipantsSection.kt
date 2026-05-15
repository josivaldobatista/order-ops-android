package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showDialog by remember { mutableStateOf(false) }

    Text(
        text = "Participantes",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(Modifier.height(8.dp))

    if (participants.isEmpty()) {
        Text(
            text = "Nenhum participante adicionado.",
            color = MaterialTheme.colorScheme.onBackground
        )
    } else {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            participants.forEach { participant ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(participant.name)
                    },
                    leadingIcon = {
                        Text(participant.name.first().uppercase())
                    }
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            showDialog = true
        },
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Adicionar participante")
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