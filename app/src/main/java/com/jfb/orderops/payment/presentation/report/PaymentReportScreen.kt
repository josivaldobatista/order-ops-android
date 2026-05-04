package com.jfb.orderops.payment.presentation.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentReportScreen(
    uiState: PaymentReportUiState,
    onLoad: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Relatório de hoje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(16.dp))
        }

        uiState.report?.let { report ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total: R$ ${"%.2f".format(report.total)}")
                    Text("Dinheiro: R$ ${"%.2f".format(report.cash)}")
                    Text("Crédito: R$ ${"%.2f".format(report.creditCard)}")
                    Text("Débito: R$ ${"%.2f".format(report.debitCard)}")
                    Text("PIX: R$ ${"%.2f".format(report.pix)}")

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Ticket médio: R$ ${"%.2f".format(report.ticketAverage)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Vendas por dia",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        if (uiState.daily.isEmpty() && !uiState.isLoading) {
            Text("Nenhuma venda encontrada para o período.")
        } else {
            val maxTotal = uiState.daily.maxOfOrNull { it.total } ?: 1.0

            uiState.daily.forEach { item ->
                val progress = if (maxTotal <= 0.0) {
                    0f
                } else {
                    (item.total / maxTotal).toFloat()
                }

                Text(item.date)

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("R$ ${"%.2f".format(item.total)}")

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}