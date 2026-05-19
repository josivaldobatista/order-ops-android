package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.jfb.orderops.order.presentation.detail.components.OrderConsumptionPreviewBottomSheet
import com.jfb.orderops.order.presentation.detail.components.OrderInfoSection
import com.jfb.orderops.order.presentation.detail.components.OrderItemsSection
import com.jfb.orderops.order.presentation.detail.components.OrderParticipantsSection
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
    events: Flow<String>,
    onNewParticipantNameChange: (String) -> Unit,
    onCreateParticipant: () -> Unit,
    onAssignItemParticipant: (Long, Long?) -> Unit
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 9.dp)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.isLoading && uiState.order == null) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
            }

            uiState.errorMessage?.let {
                OutlinedButton(
                    onClick = onRefresh,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
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
                    onBack = onBack,
                    onSendToPreparation = onSendToPreparation,
                    onMarkAsReady = onMarkAsReady,
                    onGoToPayment = onGoToPayment,
                    onCancel = onCancel,
                    onAddItem = onAddItem,
                    onRemoveItem = onRemoveItem,
                    uiState = uiState,
                    onNewParticipantNameChange = onNewParticipantNameChange,
                    onCreateParticipant = onCreateParticipant,
                    onAssignItemParticipant = onAssignItemParticipant
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OrderDetailContent(
    order: Order,
    products: List<Product>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSendToPreparation: () -> Unit,
    onMarkAsReady: () -> Unit,
    onGoToPayment: (Long, Double) -> Unit,
    onCancel: () -> Unit,
    onAddItem: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    uiState: OrderDetailUiState,
    onNewParticipantNameChange: (String) -> Unit,
    onCreateParticipant: () -> Unit,
    onAssignItemParticipant: (Long, Long?) -> Unit
) {
    var showConsumptionSheet by remember {
        mutableStateOf(false)
    }

    OrderInfoSection(
        order = order,
        participantCount = uiState.participants.size,
        isLoading = isLoading,
        onBack = onBack
    )

    Spacer(Modifier.height(12.dp))

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

    Spacer(Modifier.height(12.dp))

    OrderItemsSection(
        order = order,
        participants = uiState.participants,
        isLoading = isLoading,
        onRemoveItem = onRemoveItem,
        onAssignItemParticipant = onAssignItemParticipant
    )

    Spacer(Modifier.height(28.dp))

    OrderParticipantsSection(
        participants = uiState.participants,
        newParticipantName = uiState.newParticipantName,
        isLoading = isLoading || uiState.isCreatingParticipant,
        onNameChange = onNewParticipantNameChange,
        onAddParticipant = onCreateParticipant
    )

    if (order.status.canEditItems()) {
        Spacer(Modifier.height(28.dp))

        AddItemSection(
            products = products,
            isLoading = isLoading,
            onAddItem = onAddItem
        )
    }

    Spacer(Modifier.height(28.dp))

    val unassignedCount = uiState.consumptionPreview
        ?.unassignedItems
        ?.size
        ?: 0

    val participantCount = uiState.consumptionPreview
        ?.participants
        ?.size
        ?: 0

    val consumptionButtonText = when {
        unassignedCount > 0 -> "Ver consumo • $unassignedCount sem participante"
        participantCount > 0 -> "Ver consumo • $participantCount participante(s)"
        else -> "Ver consumo por participante"
    }

    OutlinedButton(
        onClick = {
            showConsumptionSheet = true
        },
        enabled = uiState.consumptionPreview != null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(consumptionButtonText)
    }

    if (showConsumptionSheet) {
        uiState.consumptionPreview?.let { preview ->
            OrderConsumptionPreviewBottomSheet(
                preview = preview,
                onDismiss = {
                    showConsumptionSheet = false
                }
            )
        }
    }

    Spacer(Modifier.height(32.dp))
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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
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