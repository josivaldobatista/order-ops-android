package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
    onCalculateClick: () -> Unit
) {

    Column {

        Text(
            text = "Dividir igualmente",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = peopleCount,
            onValueChange = onPeopleCountChange,
            label = {
                Text("Número de pessoas")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCalculateClick,
            enabled = !isLoading
        ) {
            Text("Calcular divisão")
        }

        splitPreview?.let { preview ->

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    preview.participants.forEach { participant ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(participant.name)

                            Text("R$ ${participant.amount}")
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}