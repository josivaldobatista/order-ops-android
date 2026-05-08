package com.jfb.orderops.order.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.presentation.state.CreateOrderItemUiState
import com.jfb.orderops.order.presentation.state.CreateOrderUiState

@Composable
fun CreateOrderScreen(
    uiState: CreateOrderUiState,
    onCategorySelected: (Long?) -> Unit,
    onAddProduct: (Long) -> Unit,
    onRemoveProduct: (Long) -> Unit,
    onCreateOrder: () -> Unit,
    onBack: () -> Unit
) {
    val filteredProducts = uiState.products.filter { product ->
        uiState.selectedCategoryId == null ||
                product.categoryId == uiState.selectedCategoryId
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
            OutlinedButton(
                onClick = onBack,
                enabled = !uiState.isLoading
            ) {
                Text("Voltar")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Novo pedido",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Categorias",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedCategoryId == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("Todos") }
                )

                uiState.categories.forEach { category ->
                    FilterChip(
                        selected = uiState.selectedCategoryId == category.id,
                        onClick = { onCategorySelected(category.id) },
                        label = { Text(category.name) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(12.dp))
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Produtos",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(filteredProducts) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(4.dp))

                            product.description?.let {
                                Text(it)
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "R$ ${"%.2f".format(product.price)}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { onAddProduct(product.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Adicionar")
                            }
                        }
                    }
                }

                if (uiState.items.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Itens do pedido",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(uiState.items) { item ->
                        OrderItemCard(
                            item = item,
                            onRemove = {
                                onRemoveProduct(item.productId)
                            }
                        )
                    }
                }
            }

            if (uiState.items.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onCreateOrder,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text("Criar pedido")
                }
            }
        }
    }
}

@Composable
private fun OrderItemCard(
    item: CreateOrderItemUiState,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(4.dp))

            Text("Quantidade: ${item.quantity}")

            Text(
                text = "Subtotal: R$ ${"%.2f".format(item.totalPrice)}"
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remover")
            }
        }
    }
}