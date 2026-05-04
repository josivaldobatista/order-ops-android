package com.jfb.orderops.serviceTable.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateServiceTableScreen(
    uiState: CreateServiceTableUiState,
    onNumberChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onCreate: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            enabled = !uiState.isLoading
        ) {
            Text("Voltar")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Nova mesa",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.number,
            onValueChange = onNumberChange,
            label = { Text("Número da mesa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.capacity,
            onValueChange = onCapacityChange,
            label = { Text("Capacidade") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(16.dp))

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = onCreate,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar mesa")
        }

        if (uiState.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}