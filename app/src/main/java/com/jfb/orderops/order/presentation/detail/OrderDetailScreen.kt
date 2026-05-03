package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.presentation.state.OrderDetailUiState
import com.jfb.orderops.product.domain.model.Product

@Composable
fun OrderDetailScreen(
    uiState: OrderDetailUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onSendToPreparation: () -> Unit,
    onMarkAsReady: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit,
    onAddItem: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Voltar")
        }

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

            Button(onClick = onRefresh) {
                Text("Tentar novamente")
            }
        }

        uiState.order?.let { order ->
            OrderDetailContent(
                order = order,
                products = uiState.products,
                onSendToPreparation = onSendToPreparation,
                onMarkAsReady = onMarkAsReady,
                onFinish = onFinish,
                onCancel = onCancel,
                onAddItem = onAddItem,
                onRemoveItem = onRemoveItem
            )
        }
    }
}

@Composable
private fun OrderDetailContent(
    order: Order,
    products: List<Product>,
    onSendToPreparation: () -> Unit,
    onMarkAsReady: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit,
    onAddItem: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit
) {
    Text(
        text = "Pedido #${order.id}",
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(Modifier.height(16.dp))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Mesa: ${order.serviceTableId}")
            Text("Status: ${order.status.toReadableText()}")
            Text("Total: R$ ${"%.2f".format(order.totalAmount)}")
        }
    }

    Spacer(Modifier.height(16.dp))

    OrderStatusActions(
        status = order.status,
        onSendToPreparation = onSendToPreparation,
        onMarkAsReady = onMarkAsReady,
        onFinish = onFinish,
        onCancel = onCancel
    )

    if (order.status == OrderStatus.OPEN) {
        Spacer(Modifier.height(24.dp))

        AddItemSection(
            products = products,
            onAddItem = onAddItem
        )
    }

    Spacer(Modifier.height(24.dp))

    Text(
        text = "Itens",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(Modifier.height(8.dp))

    if (order.items.isEmpty()) {
        Text("Nenhum item no pedido.")
    } else {
        order.items.forEach { item ->
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
                    Column {
                        Text(
                            text = item.productName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Quantidade: ${item.quantity}")
                        Text("Unitário: R$ ${"%.2f".format(item.unitPrice)}")
                        Text("Subtotal: R$ ${"%.2f".format(item.totalPrice)}")
                    }

                    if (order.status == OrderStatus.OPEN) {
                        TextButton(
                            onClick = { onRemoveItem(item.id) }
                        ) {
                            Text("Remover")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemSection(
    products: List<Product>,
    onAddItem: (Long, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedProductId by remember(products) {
        mutableStateOf(products.firstOrNull()?.id)
    }
    var quantity by remember { mutableStateOf(1) }

    val selectedProduct = products.firstOrNull {
        it.id == selectedProductId
    }

    Text(
        text = "Adicionar item",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(8.dp))

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
            products.forEach { product ->
                DropdownMenuItem(
                    text = {
                        Text("${product.name} - R$ ${"%.2f".format(product.price)}")
                    },
                    onClick = {
                        selectedProductId = product.id
                        expanded = false
                    }
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {
                quantity = maxOf(1, quantity - 1)
            }
        ) {
            Text("-")
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = {
                quantity++
            }
        ) {
            Text("+")
        }
    }

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            selectedProductId?.let { productId ->
                onAddItem(productId, quantity)
                quantity = 1
            }
        },
        enabled = selectedProductId != null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Adicionar item")
    }
}

@Composable
private fun OrderStatusActions(
    status: OrderStatus,
    onSendToPreparation: () -> Unit,
    onMarkAsReady: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (status) {
            OrderStatus.OPEN -> {
                Button(
                    onClick = onSendToPreparation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar para preparo")
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.IN_PREPARATION -> {
                Button(
                    onClick = onMarkAsReady,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como pronto")
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.READY -> {
                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar pedido")
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.FINISHED -> {
                Text("Pedido finalizado.")
            }

            OrderStatus.CANCELLED -> {
                Text("Pedido cancelado.")
            }

            OrderStatus.UNKNOWN -> {
                Text("Status desconhecido.")
            }
        }
    }
}

private fun OrderStatus.toReadableText(): String {
    return when (this) {
        OrderStatus.OPEN -> "Aberto"
        OrderStatus.IN_PREPARATION -> "Em preparo"
        OrderStatus.READY -> "Pronto"
        OrderStatus.FINISHED -> "Finalizado"
        OrderStatus.CANCELLED -> "Cancelado"
        OrderStatus.UNKNOWN -> "Desconhecido"
    }
}