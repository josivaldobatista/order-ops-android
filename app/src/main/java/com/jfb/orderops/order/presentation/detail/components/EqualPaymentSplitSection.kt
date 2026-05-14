package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewResponse

@Composable
fun EqualPaymentSplitSection(
    peopleCount: String,
    splitPreview: PaymentSplitPreviewResponse?,
    isLoading: Boolean,
    onPeopleCountChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onDismissPreview: () -> Unit
) {
    Column {
        Text(
            text = "Dividir igualmente",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = peopleCount,
            onValueChange = onPeopleCountChange,
            label = { Text("Número de pessoas") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onCalculateClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) {
                    "Calculando..."
                } else {
                    "Calcular divisão"
                }
            )
        }
    }

    splitPreview?.let { preview ->
        AlertDialog(
            onDismissRequest = onDismissPreview,
            title = {
                Text("Divisão igual")
            },
            text = {
                Column {
                    Text(
                        text = "Total: R$ ${"%.2f".format(preview.totalAmount)}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    preview.participants.forEach { participant ->
                        Text(
                            text = "${participant.name}: R$ ${"%.2f".format(participant.amount)}"
                        )

                        Spacer(Modifier.height(6.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onDismissPreview
                ) {
                    Text("Fechar")
                }
            }
        )
    }
}