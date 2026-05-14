package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.model.OrderStatus
import com.jfb.orderops.order.presentation.detail.components.OrderInfoSection
import com.jfb.orderops.order.presentation.detail.components.OrderItemsSection
import com.jfb.orderops.order.presentation.detail.components.OrderStatusActionsSection
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
    onGoToPayment: (Long, Double) -> Unit,
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
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
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
                    onGoToPayment = onGoToPayment,
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
    onGoToPayment: (Long, Double) -> Unit,
    onCancel: () -> Unit,
    onAddItem: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
) {
    OrderInfoSection(order = order)

    Spacer(Modifier.height(16.dp))

    OrderStatusActionsSection(
        status = order.status,
        isLoading = isLoading,
        onSendToPreparation = onSendToPreparation,
        onMarkAsReady = onMarkAsReady,
        onFinish = {
            onGoToPayment(order.id, order.totalAmount)
        },
        onCancel = onCancel
    )

    if (order.status.canEditItems()) {
        Spacer(Modifier.height(24.dp))

        AddItemSection(
            products = products,
            isLoading = isLoading,
            onAddItem = onAddItem
        )
    }

    Spacer(Modifier.height(24.dp))

    OrderItemsSection(
        order = order,
        isLoading = isLoading,
        onRemoveItem = onRemoveItem
    )

    Spacer(Modifier.height(24.dp))

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
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
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
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
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

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}