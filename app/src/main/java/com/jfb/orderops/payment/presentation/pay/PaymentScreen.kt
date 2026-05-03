package com.jfb.orderops.payment.presentation.pay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.payment.domain.model.PaymentMethod
import com.jfb.orderops.payment.presentation.state.PaymentUiState

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    onMethodSelected: (PaymentMethod) -> Unit,
    onPayClick: () -> Unit,
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
            text = "Pagamento do pedido #${uiState.orderId}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Total: R$ ${"%.2f".format(uiState.amount)}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Forma de pagamento",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentMethod.entries.forEach { method ->
                FilterChip(
                    selected = uiState.selectedMethod == method,
                    onClick = { onMethodSelected(method) },
                    enabled = !uiState.isLoading,
                    label = {
                        Text(method.toReadableText())
                    }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = onPayClick,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar pagamento")
        }

        if (uiState.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

private fun PaymentMethod.toReadableText(): String {
    return when (this) {
        PaymentMethod.CASH -> "Dinheiro"
        PaymentMethod.CREDIT_CARD -> "Cartão de crédito"
        PaymentMethod.DEBIT_CARD -> "Cartão de débito"
        PaymentMethod.PIX -> "PIX"
    }
}