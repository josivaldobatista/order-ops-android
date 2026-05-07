package com.jfb.orderops.order.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.presentation.state.CreateOrderUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    uiState: CreateOrderUiState,
    onProductSelected: (Long) -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onAddProduct: () -> Unit,
    onRemoveProduct: (Long) -> Unit,
    onCreateOrder: () -> Unit,
    onBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedProduct = uiState.products.firstOrNull {
        it.id == uiState.selectedProductId
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = "Novo pedido - Mesa ${uiState.serviceTableId}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text("Voltar")
            }

            Spacer(Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedProduct?.name ?: "Selecione um produto",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Produto") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.products.forEach { product ->
                        DropdownMenuItem(
                            text = {
                                Text("${product.name} - R$ ${"%.2f".format(product.price)}")
                            },
                            onClick = {
                                onProductSelected(product.id)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onDecreaseQuantity) {
                    Text("-")
                }

                Text(
                    text = uiState.selectedQuantity.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(onClick = onIncreaseQuantity) {
                    Text("+")
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onAddProduct,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar produto")
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Itens adicionados",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            if (uiState.addedItems.isEmpty()) {
                Text("Nenhum produto adicionado.")
            } else {
                uiState.addedItems.forEach { (productId, quantity) ->
                    val product = uiState.products.firstOrNull { it.id == productId }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${quantity}x ${product?.name ?: "Produto $productId"}")

                            TextButton(
                                onClick = { onRemoveProduct(productId) }
                            ) {
                                Text("Remover")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = onCreateOrder,
                enabled = !uiState.isCreatingOrder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (uiState.isCreatingOrder) {
                        "Criando..."
                    } else {
                        "Criar pedido"
                    }
                )
            }

            if (uiState.isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}