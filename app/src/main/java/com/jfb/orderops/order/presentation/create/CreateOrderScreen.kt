package com.jfb.orderops.order.presentation.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.core.ui.components.ComandexScaffold
import com.jfb.orderops.order.domain.model.OrderFulfillmentType
import com.jfb.orderops.order.presentation.state.CreateOrderItemUiState
import com.jfb.orderops.order.presentation.state.CreateOrderUiState
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors
import com.jfb.orderops.ui.theme.OrderOpsTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    uiState: CreateOrderUiState,
    onFulfillmentTypeSelected: (OrderFulfillmentType) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onAddProduct: (Long) -> Unit,
    onRemoveProduct: (Long) -> Unit,
    onCreateOrder: () -> Unit,
    onBack: () -> Unit
) {
    val totalItems = uiState.items.sumOf { it.quantity }
    val totalAmount = uiState.items.sumOf { it.totalPrice }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val isExpanded =
        scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded

    ComandexScaffold {
        if (uiState.items.isEmpty()) {
            CreateOrderContent(
                uiState = uiState,
                bottomPadding = 24.dp,
                onFulfillmentTypeSelected = onFulfillmentTypeSelected,
                onCategorySelected = onCategorySelected,
                onAddProduct = onAddProduct,
                onRemoveProduct = onRemoveProduct,
                onBack = onBack
            )
        } else {
            BottomSheetScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 18.dp),
                scaffoldState = scaffoldState,
                sheetPeekHeight = 132.dp,
                sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                sheetContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                sheetDragHandle = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 18.dp, end = 18.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(42.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f))
                                .align(Alignment.Center)
                        )

                        Icon(
                            painter = painterResource(
                                if (isExpanded)
                                    R.drawable.ic_arrow_down
                                else
                                    R.drawable.ic_arrow_up
                            ),
                            contentDescription = "Expandir carrinho",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                },
                sheetContent = {
                    OrderCartSheetContent(
                        items = uiState.items,
                        totalItems = totalItems,
                        totalAmount = totalAmount,
                        isLoading = uiState.isLoading,
                        onAddProduct = onAddProduct,
                        onRemoveProduct = onRemoveProduct,
                        onCreateOrder = onCreateOrder
                    )
                },
                containerColor = Color.Transparent
            ) { padding ->
                CreateOrderContent(
                    uiState = uiState,
                    bottomPadding = padding.calculateBottomPadding() + 16.dp,
                    onFulfillmentTypeSelected = onFulfillmentTypeSelected,
                    onCategorySelected = onCategorySelected,
                    onAddProduct = onAddProduct,
                    onRemoveProduct = onRemoveProduct,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun CreateOrderContent(
    uiState: CreateOrderUiState,
    bottomPadding: Dp,
    onFulfillmentTypeSelected: (OrderFulfillmentType) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onAddProduct: (Long) -> Unit,
    onRemoveProduct: (Long) -> Unit,
    onBack: () -> Unit
) {
    val filteredProducts = uiState.products.filter { product ->
        product.active &&
                (uiState.selectedCategoryId == null || product.categoryId == uiState.selectedCategoryId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp)
    ) {
        item {
            CreateOrderHeader(
                uiState = uiState,
                onBack = onBack
            )
        }

        item {
            OrderContextBar(
                selectedType = uiState.fulfillmentType,
                serviceTableId = uiState.serviceTableId,
                enabled = !uiState.isLoading,
                onFulfillmentTypeSelected = onFulfillmentTypeSelected,
                onChangeTable = onBack
            )
        }

        item {
            CategorySelector(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategorySelected
            )
        }

        uiState.errorMessage?.let { message ->
            item {
                ErrorMessage(message = message)
            }
        }

        if (uiState.selectedCategoryId == null) {
            uiState.categories
                .filter { it.active }
                .forEach { category ->
                    val productsByCategory = uiState.products.filter { product ->
                        product.active && product.categoryId == category.id
                    }

                    if (productsByCategory.isNotEmpty()) {
                        item(key = "category-${category.id}") {
                            CategorySectionTitle(title = category.name)
                        }

                        items(
                            items = productsByCategory,
                            key = { product -> product.id }
                        ) { product ->
                            val quantity = uiState.items
                                .firstOrNull { it.productId == product.id }
                                ?.quantity ?: 0

                            ProductOrderCard(
                                product = product,
                                quantity = quantity,
                                onAdd = { onAddProduct(product.id) },
                                onDecrease = { onRemoveProduct(product.id) }
                            )
                        }
                    }
                }
        } else {
            item {
                CategorySectionTitle(
                    title = selectedCategoryTitle(
                        categories = uiState.categories,
                        selectedCategoryId = uiState.selectedCategoryId
                    )
                )
            }

            items(
                items = filteredProducts,
                key = { product -> product.id }
            ) { product ->
                val quantity = uiState.items
                    .firstOrNull { it.productId == product.id }
                    ?.quantity ?: 0

                ProductOrderCard(
                    product = product,
                    quantity = quantity,
                    onAdd = { onAddProduct(product.id) },
                    onDecrease = { onRemoveProduct(product.id) }
                )
            }
        }

        if (!uiState.isLoading && filteredProducts.isEmpty() && uiState.selectedCategoryId != null) {
            item {
                EmptyProductsMessage()
            }
        }
    }
}

@Composable
private fun CreateOrderHeader(
    uiState: CreateOrderUiState,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            enabled = !uiState.isLoading,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface.copy(alpha = 0.48f))
                .border(
                    width = 1.dp,
                    color = colors.outline.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(14.dp)
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = "Voltar",
                tint = colors.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = headerTitle(uiState),
                style = MaterialTheme.typography.titleMedium,
                color = colors.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(extraColors.success)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = headerSubtitle(uiState),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun OrderContextBar(
    selectedType: OrderFulfillmentType,
    serviceTableId: Long?,
    enabled: Boolean,
    onFulfillmentTypeSelected: (OrderFulfillmentType) -> Unit,
    onChangeTable: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surface.copy(alpha = 0.42f),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(alpha = 0.14f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = enabled) {
                            expanded = true
                        }
                        .background(colors.background.copy(alpha = 0.20f))
                        .border(
                            width = 1.dp,
                            color = colors.outline.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(selectedType.iconRes()),
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(17.dp)
                    )

                    Text(
                        text = selectedType.shortLabel(),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "⌄",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = colors.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    OrderFulfillmentType.entries
                        .filter { it != OrderFulfillmentType.UNKNOWN }
                        .forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = type.shortLabel(),
                                        color = colors.onSurface
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(type.iconRes()),
                                        contentDescription = null,
                                        tint = if (type == selectedType) colors.primary else colors.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    onFulfillmentTypeSelected(type)
                                }
                            )
                        }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (selectedType == OrderFulfillmentType.DINE_IN) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = enabled) {
                            onChangeTable()
                        }
                        .background(colors.background.copy(alpha = 0.20f))
                        .border(
                            width = 1.dp,
                            color = colors.outline.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_turntable),
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(17.dp)
                    )

                    Text(
                        text = if (serviceTableId != null) "Mesa $serviceTableId" else "Alterar mesa",
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySelector(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit
) {
    Column {
        Text(
            text = "Categorias",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            item {
                CategoryChip(
                    text = "Todas",
                    selected = selectedCategoryId == null,
                    onClick = { onCategorySelected(null) }
                )
            }

            items(
                items = categories.filter { it.active },
                key = { it.id }
            ) { category ->
                CategoryChip(
                    text = category.name,
                    selected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) colors.primary.copy(alpha = 0.18f)
                else colors.surface.copy(alpha = 0.38f)
            )
            .border(
                width = 1.dp,
                color = if (selected) colors.primary else colors.outline.copy(alpha = 0.18f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) colors.primary else colors.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun CategorySectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ProductOrderCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onDecrease: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.82f),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePlaceholder(name = product.name)

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = product.price.toCurrency(),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description.orEmpty().ifBlank {
                        product.categoryName ?: "Produto do cardápio"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                modifier = Modifier.padding(end = 6.dp)
            ) {
                QuantityControl(
                    quantity = quantity,
                    onAdd = onAdd,
                    onDecrease = onDecrease
                )
            }
        }
    }
}

@Composable
private fun ProductImagePlaceholder(name: String) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceVariant.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.primary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun QuantityControl(
    quantity: Int,
    onAdd: () -> Unit,
    onDecrease: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    if (quantity <= 0) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.primary.copy(alpha = 0.14f))
                .border(
                    width = 1.dp,
                    color = colors.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onAdd() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleMedium,
                color = colors.primary,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    Row(
        modifier = Modifier
            .height(26.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.background.copy(alpha = 0.24f))
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.18f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onDecrease,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(22.dp)
        ) {
            if (quantity == 1) {

                Icon(
                    painter = painterResource(R.drawable.ic_trash_2),
                    contentDescription = "Remover item",
                    tint = colors.error,
                    modifier = Modifier.size(18.dp)
                )

            } else {

                Text(
                    text = "−",
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        TextButton(
            onClick = onAdd,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(30.dp)
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )
        }
    }
}

@Composable
private fun OrderCartSheetContent(
    items: List<CreateOrderItemUiState>,
    totalItems: Int,
    totalAmount: Double,
    isLoading: Boolean,
    onAddProduct: (Long) -> Unit,
    onRemoveProduct: (Long) -> Unit,
    onCreateOrder: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.background.copy(alpha = 0.34f))
                    .border(
                        width = 1.dp,
                        color = colors.outline.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_basket),
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$totalItems ${if (totalItems == 1) "item" else "itens"}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = totalAmount.toCurrency(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onCreateOrder,
                enabled = !isLoading,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 13.dp)
            ) {
                Text(
                    text = if (isLoading) "Criando..." else "Criar pedido",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Itens adicionados",
            style = MaterialTheme.typography.titleMedium,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                CartItemRow(
                    item = item,
                    onAdd = { onAddProduct(item.productId) },
                    onDecrease = { onRemoveProduct(item.productId) }
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
private fun CartItemRow(
    item: CreateOrderItemUiState,
    onAdd: () -> Unit,
    onDecrease: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.background.copy(alpha = 0.26f),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${item.quantity} x ${item.unitPrice.toCurrency()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            Text(
                text = item.totalPrice.toCurrency(),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            QuantityControl(
                quantity = item.quantity,
                onAdd = onAdd,
                onDecrease = onDecrease
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.16f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.32f)
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun EmptyProductsMessage() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        )
    ) {
        Text(
            text = "Nenhum produto encontrado nesta categoria.",
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun headerTitle(uiState: CreateOrderUiState): String {
    return if (uiState.fulfillmentType == OrderFulfillmentType.DINE_IN && uiState.serviceTableId != null) {
        "Pedido da Mesa ${uiState.serviceTableId}"
    } else {
        "Novo pedido"
    }
}

private fun headerSubtitle(uiState: CreateOrderUiState): String {
    return when (uiState.fulfillmentType) {
        OrderFulfillmentType.DINE_IN -> "Atendimento local"
        OrderFulfillmentType.TAKEOUT -> "Retirada no balcão"
        OrderFulfillmentType.DELIVERY -> "Pedido para entrega"
        OrderFulfillmentType.UNKNOWN -> "Monte a comanda"
    }
}

private fun selectedCategoryTitle(
    categories: List<Category>,
    selectedCategoryId: Long?
): String {
    return categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Produtos"
}

private fun OrderFulfillmentType.shortLabel(): String {
    return when (this) {
        OrderFulfillmentType.DINE_IN -> "Local"
        OrderFulfillmentType.TAKEOUT -> "Retirada"
        OrderFulfillmentType.DELIVERY -> "Entrega"
        OrderFulfillmentType.UNKNOWN -> "Outro"
    }
}

private fun OrderFulfillmentType.iconRes(): Int {
    return when (this) {
        OrderFulfillmentType.DINE_IN -> R.drawable.ic_utensils_crossed
        OrderFulfillmentType.TAKEOUT -> R.drawable.ic_basket
        OrderFulfillmentType.DELIVERY -> R.drawable.ic_motorbike
        OrderFulfillmentType.UNKNOWN -> R.drawable.ic_receipt
    }
}

private fun Double.toCurrency(): String {
    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(this)
}

@Preview(showBackground = true)
@Composable
private fun CreateOrderScreenPreview() {
    OrderOpsTheme {
        CreateOrderScreen(
            uiState = CreateOrderUiState(
                serviceTableId = 1L,
                fulfillmentType = OrderFulfillmentType.DINE_IN,
                categories = listOf(
                    Category(id = 1L, name = "🍔 Hambúrgueres", active = true),
                    Category(id = 2L, name = "🥤 Bebidas", active = true)
                ),
                products = listOf(
                    Product(
                        id = 1L,
                        name = "X-Burger",
                        categoryId = 1L,
                        categoryName = "Hambúrgueres",
                        description = "Hambúrguer artesanal com queijo",
                        price = 29.90,
                        active = true
                    ),
                    Product(
                        id = 2L,
                        name = "Hambúrguer Artesanal",
                        categoryId = 1L,
                        categoryName = "Hambúrgueres",
                        description = "Hambúrguer de carne bovina 180g, queijo cheddar e cebola caramelizada",
                        price = 29.50,
                        active = true
                    ),
                    Product(
                        id = 3L,
                        name = "Coca-Cola 2L",
                        categoryId = 2L,
                        categoryName = "Bebidas",
                        description = "Refrigerante Coca-Cola de 2L Original",
                        price = 14.90,
                        active = true
                    )
                ),
                selectedCategoryId = null,
                items = listOf(
                    CreateOrderItemUiState(
                        productId = 1L,
                        productName = "X-Burger",
                        quantity = 1,
                        unitPrice = 29.90
                    )
                )
            ),
            onFulfillmentTypeSelected = {},
            onCategorySelected = {},
            onAddProduct = {},
            onRemoveProduct = {},
            onCreateOrder = {},
            onBack = {}
        )
    }
}