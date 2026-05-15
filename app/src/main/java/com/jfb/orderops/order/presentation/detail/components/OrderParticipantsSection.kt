package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Text(
        text = "Participantes",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = newParticipantName,
        onValueChange = onNameChange,
        label = { Text("Nome do participante") },
        enabled = !isLoading,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))

    Button(
        onClick = onAddParticipant,
        enabled = !isLoading && newParticipantName.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Adicionar participante")
    }

    Spacer(Modifier.height(12.dp))

    if (participants.isEmpty()) {
        Text(
            text = "Nenhum participante adicionado.",
            color = MaterialTheme.colorScheme.onBackground
        )
    } else {
        Column {
            participants.forEach { participant ->
                Text(
                    text = "• ${participant.name}",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}