package com.jfb.orderops.serviceTable.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.model.ServiceTableStatus
import com.jfb.orderops.serviceTable.presentation.state.ServiceTablesUiState

@Composable
fun ServiceTablesScreen(
    uiState: ServiceTablesUiState,
    onRefresh: () -> Unit,
    onTableClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mesas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRefresh,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar mesas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.serviceTables) { serviceTable ->
                ServiceTableCard(
                    serviceTable = serviceTable,
                    onClick = { onTableClick(serviceTable.id) }
                )
            }
        }
    }
}

@Composable
private fun ServiceTableCard(
    serviceTable: ServiceTable,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Mesa ${serviceTable.number}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Capacidade: ${serviceTable.capacity}")
            Text("Status: ${serviceTable.status.toReadableText()}")

            Spacer(modifier = Modifier.height(12.dp))

            val canCreateOrder = serviceTable.status == ServiceTableStatus.AVAILABLE ||
                    serviceTable.status == ServiceTableStatus.RESERVED

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = canCreateOrder
            ) {
                Text("Criar pedido")
            }

            if (!canCreateOrder) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mesa indisponível para pedido",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun ServiceTableStatus.toReadableText(): String {
    return when (this) {
        ServiceTableStatus.AVAILABLE -> "Disponível"
        ServiceTableStatus.OCCUPIED -> "Ocupada"
        ServiceTableStatus.RESERVED -> "Reservada"
        ServiceTableStatus.INACTIVE -> "Inativa"
        ServiceTableStatus.UNKNOWN -> "Desconhecido"
    }
}