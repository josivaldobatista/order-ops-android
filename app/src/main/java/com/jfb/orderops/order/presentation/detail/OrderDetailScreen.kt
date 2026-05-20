package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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

    Spacer(Modifier.height(6.dp))

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
    var showProductSheet by remember { mutableStateOf(false) }

    var selectedProductId by remember(products) {
        mutableStateOf(products.firstOrNull()?.id)
    }

    var selectedCategoryName by remember {
        mutableStateOf<String?>(null)
    }

    var quantity by remember { mutableStateOf(1) }

    val selectedProduct = products.firstOrNull { it.id == selectedProductId }

    Text(
        text = "Adicionar item",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.42f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductSelectorButton(
                product = selectedProduct,
                enabled = !isLoading,
                onClick = { showProductSheet = true },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            QuantityMiniButton(
                text = "−",
                enabled = !isLoading,
                onClick = { quantity = maxOf(1, quantity - 1) }
            )

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            QuantityMiniButton(
                text = "+",
                enabled = !isLoading,
                onClick = { quantity++ }
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    selectedProductId?.let { productId ->
                        onAddItem(productId, quantity)
                        quantity = 1
                    }
                },
                enabled = selectedProductId != null && !isLoading,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                modifier = Modifier.height(42.dp)
            ) {
                Text(
                    text = "Adicionar",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showProductSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProductSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                val categoryNames = products
                    .mapNotNull { it.categoryName }
                    .distinct()

                CategoryFilterRow(
                    categories = categoryNames,
                    selectedCategoryName = selectedCategoryName,
                    onCategorySelected = { selectedCategoryName = it }
                )

                Spacer(Modifier.height(14.dp))

                val filteredProducts = if (selectedCategoryName == null) {
                    products
                } else {
                    products.filter { it.categoryName == selectedCategoryName }
                }

                Text(
                    text = "Selecionar produto",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                val productsByCategory = filteredProducts
                    .groupBy { it.categoryName ?: "Produtos" }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    productsByCategory.forEach { (categoryName, categoryProducts) ->

                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categoryProducts.forEach { product ->
                                ProductSheetRow(
                                    product = product,
                                    selected = product.id == selectedProductId,
                                    onClick = {
                                        selectedProductId = product.id
                                        showProductSheet = false
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))
                    }
                }

                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<String>,
    selectedCategoryName: String?,
    onCategorySelected: (String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CategoryFilterChip(
                text = "Todas",
                selected = selectedCategoryName == null,
                onClick = { onCategorySelected(null) }
            )
        }

        items(categories) { category ->
            CategoryFilterChip(
                text = category,
                selected = selectedCategoryName == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f)
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
            }
        ),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProductSelectorButton(
    product: Product?,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(42.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.28f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🍔",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = product?.name ?: "Selecionar produto",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "⌄",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductSheetRow(
    product: Product,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
        } else {
            MaterialTheme.colorScheme.background.copy(alpha = 0.28f)
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.36f)
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.name.firstOrNull()?.uppercase() ?: "?",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.categoryName ?: "Produto",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "R$ ${"%.2f".format(product.price)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuantityMiniButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(34.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun OrderStatus.canEditItems(): Boolean {
    return this == OrderStatus.OPEN ||
            this == OrderStatus.IN_PREPARATION ||
            this == OrderStatus.READY
}