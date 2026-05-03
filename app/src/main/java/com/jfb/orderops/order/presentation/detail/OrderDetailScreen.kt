package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.presentation.state.OrderDetailUiState
import com.jfb.orderops.product.domain.model.Product
import kotlinx.coroutines.flow.Flow

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
    onRemoveItem: (Long) -> Unit,
    events: Flow<String>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = onBack,
                enabled = !uiState.isLoading
            ) {
                Text("Voltar")
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
            }

            uiState.errorMessage?.let {
                Button(
                    onClick = onRefresh,
                    enabled = !uiState.isLoading
                ) {
                    Text("Tentar novamente")
                }

                Spacer(Modifier.height(16.dp))
            }

            uiState.order?.let { order ->
                OrderDetailContent(
                    order = order,
                    products = uiState.products,
                    isLoading = uiState.isLoading,
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
}

@Composable
private fun OrderDetailContent(
    order: Order,
    products: List<Product>,
    isLoading: Boolean,
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
            Text(
                text = "Status: ${order.status.toReadableText()}",
                color = order.status.toColor()
            )
            Text("Total: R$ ${"%.2f".format(order.totalAmount)}")
        }
    }

    Spacer(Modifier.height(16.dp))

    OrderStatusActions(
        status = order.status,
        isLoading = isLoading,
        onSendToPreparation = onSendToPreparation,
        onMarkAsReady = onMarkAsReady,
        onFinish = onFinish,
        onCancel = onCancel
    )

    if (order.status == OrderStatus.OPEN) {
        Spacer(Modifier.height(24.dp))

        AddItemSection(
            products = products,
            isLoading = isLoading,
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
                            onClick = { onRemoveItem(item.id) },
                            enabled = !isLoading
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
    isLoading: Boolean,
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
        onExpandedChange = {
            if (!isLoading) {
                expanded = !expanded
            }
        }
    ) {
        OutlinedTextField(
            value = selectedProduct?.name ?: "Selecione um produto",
            onValueChange = {},
            readOnly = true,
            enabled = !isLoading,
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
                    },
                    enabled = !isLoading
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
            },
            enabled = !isLoading
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
            },
            enabled = !isLoading
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
        enabled = selectedProductId != null && !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Adicionar item")
    }
}

@Composable
private fun OrderStatusActions(
    status: OrderStatus,
    isLoading: Boolean,
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
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar para preparo")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.IN_PREPARATION -> {
                Button(
                    onClick = onMarkAsReady,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como pronto")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }

            OrderStatus.READY -> {
                Button(
                    onClick = onFinish,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar pedido")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
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

private fun OrderStatus.toColor(): Color {
    return when (this) {
        OrderStatus.OPEN -> Color(0xFF4CAF50)          // verde
        OrderStatus.IN_PREPARATION -> Color(0xFFFFC107) // amarelo
        OrderStatus.READY -> Color(0xFF2196F3)         // azul
        OrderStatus.FINISHED -> Color(0xFF9E9E9E)      // cinza
        OrderStatus.CANCELLED -> Color(0xFFF44336)     // vermelho
        OrderStatus.UNKNOWN -> Color.DarkGray
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