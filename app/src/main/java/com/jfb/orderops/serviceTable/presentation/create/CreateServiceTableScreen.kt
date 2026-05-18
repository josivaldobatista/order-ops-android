package com.jfb.orderops.serviceTable.presentation.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R

@Composable
fun CreateServiceTableScreen(
    uiState: CreateServiceTableUiState,
    onNumberChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onCreate: () -> Unit,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            CreateTableHeader(
                isLoading = uiState.isLoading,
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colors.surface.copy(alpha = 0.88f),
                border = BorderStroke(
                    width = 1.dp,
                    brush = SolidColor(colors.outline.copy(alpha = 0.20f))
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(
                                colors.primary.copy(alpha = 0.12f),
                                CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = colors.primary.copy(alpha = 0.45f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_turntable),
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Dados da mesa",
                        color = colors.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Informe o número e a capacidade para operação.",
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    OutlinedTextField(
                        value = uiState.number,
                        onValueChange = onNumberChange,
                        label = { Text("Número da mesa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(18.dp),
                        colors = tableTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.capacity,
                        onValueChange = onCapacityChange,
                        label = { Text("Capacidade") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(18.dp),
                        colors = tableTextFieldColors()
                    )

                    uiState.errorMessage?.let { message ->

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = colors.error.copy(alpha = 0.12f),
                            border = BorderStroke(
                                width = 1.dp,
                                color = colors.error.copy(alpha = 0.25f)
                            )
                        ) {
                            Text(
                                text = message,
                                color = colors.error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onCreate,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary,
                            disabledContainerColor = colors.surfaceVariant,
                            disabledContentColor = colors.onSurfaceVariant
                        )
                    ) {

                        if (uiState.isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = colors.onPrimary
                            )

                        } else {

                            Icon(
                                imageVector = Icons.Rounded.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Salvar mesa",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateTableHeader(
    isLoading: Boolean,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            onClick = onBack,
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp),
            color = colors.surface.copy(alpha = 0.72f),
            border = BorderStroke(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.24f)
            )
        ) {

            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Voltar",
                tint = colors.onSurface,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {

            Text(
                text = "Nova mesa",
                color = colors.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Cadastro operacional",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun tableTextFieldColors(): TextFieldColors {

    val colors = MaterialTheme.colorScheme

    return OutlinedTextFieldDefaults.colors(

        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        disabledTextColor = colors.onSurfaceVariant,

        focusedContainerColor = colors.surfaceVariant.copy(alpha = 0.55f),
        unfocusedContainerColor = colors.surfaceVariant.copy(alpha = 0.38f),
        disabledContainerColor = colors.surfaceVariant.copy(alpha = 0.28f),

        focusedBorderColor = colors.primary.copy(alpha = 0.86f),
        unfocusedBorderColor = colors.outline.copy(alpha = 0.28f),
        disabledBorderColor = colors.outline.copy(alpha = 0.12f),

        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.onSurfaceVariant,
        disabledLabelColor = colors.onSurfaceVariant.copy(alpha = 0.6f),

        cursorColor = colors.primary
    )
}