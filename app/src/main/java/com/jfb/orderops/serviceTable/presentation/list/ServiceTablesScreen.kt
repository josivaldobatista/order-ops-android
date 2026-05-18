package com.jfb.orderops.serviceTable.presentation.list

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.model.ServiceTableStatus
import com.jfb.orderops.serviceTable.presentation.state.ServiceTablesUiState
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors

private enum class TableFilter {
    ALL,
    AVAILABLE,
    OCCUPIED,
    UNAVAILABLE
}

@Composable
fun ServiceTablesScreen(
    uiState: ServiceTablesUiState,
    onRefresh: () -> Unit,
    onTableClick: (Long) -> Unit,
    onCreateTableClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(TableFilter.ALL) }

    val tables = uiState.serviceTables
    val filteredTables = remember(tables, selectedFilter) {
        when (selectedFilter) {
            TableFilter.ALL -> tables
            TableFilter.AVAILABLE -> tables.filter {
                it.status == ServiceTableStatus.AVAILABLE || it.status == ServiceTableStatus.RESERVED
            }
            TableFilter.OCCUPIED -> tables.filter {
                it.status == ServiceTableStatus.OCCUPIED
            }
            TableFilter.UNAVAILABLE -> tables.filter {
                it.status == ServiceTableStatus.INACTIVE || it.status == ServiceTableStatus.UNKNOWN
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TablesHeader(
                isLoading = uiState.isLoading,
                onRefresh = onRefresh
            )

            Spacer(modifier = Modifier.height(20.dp))

            TableFilters(
                selectedFilter = selectedFilter,
                tables = tables,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                uiState.isLoading && tables.isEmpty() -> {
                    LoadingTablesState()
                }

                uiState.errorMessage != null && tables.isEmpty() -> {
                    ErrorTablesState(
                        message = uiState.errorMessage,
                        onRetry = onRefresh
                    )
                }

                tables.isEmpty() -> {
                    EmptyTablesState()
                }

                filteredTables.isEmpty() -> {
                    EmptyFilteredTablesState()
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 112.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            items = filteredTables,
                            key = { it.id }
                        ) { serviceTable ->
                            ServiceTableCard(
                                serviceTable = serviceTable,
                                onClick = { onTableClick(serviceTable.id) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onCreateTableClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 108.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Nova mesa"
            )
        }
    }
}

@Composable
private fun TablesHeader(
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Mesas",
                color = colors.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Gerencie mesas e comandas",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Surface(
            onClick = onRefresh,
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp),
            color = colors.surface.copy(alpha = 0.72f),
            border = BorderStroke(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.26f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Atualizar mesas",
                    tint = colors.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "Atualizar",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TableFilters(
    selectedFilter: TableFilter,
    tables: List<ServiceTable>,
    onFilterSelected: (TableFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TableFilterChip(
            text = "Todas",
            count = tables.size,
            selected = selectedFilter == TableFilter.ALL,
            onClick = { onFilterSelected(TableFilter.ALL) }
        )

        TableFilterChip(
            text = "Livre",
            count = tables.count {
                it.status == ServiceTableStatus.AVAILABLE || it.status == ServiceTableStatus.RESERVED
            },
            selected = selectedFilter == TableFilter.AVAILABLE,
            statusColor = LocalOrderOpsExtraColors.current.success,
            onClick = { onFilterSelected(TableFilter.AVAILABLE) }
        )

        TableFilterChip(
            text = "Ocupada",
            count = tables.count { it.status == ServiceTableStatus.OCCUPIED },
            selected = selectedFilter == TableFilter.OCCUPIED,
            statusColor = LocalOrderOpsExtraColors.current.warning,
            onClick = { onFilterSelected(TableFilter.OCCUPIED) }
        )

        TableFilterChip(
            text = "Indisp.",
            count = tables.count {
                it.status == ServiceTableStatus.INACTIVE || it.status == ServiceTableStatus.UNKNOWN
            },
            selected = selectedFilter == TableFilter.UNAVAILABLE,
            statusColor = MaterialTheme.colorScheme.error,
            onClick = { onFilterSelected(TableFilter.UNAVAILABLE) }
        )
    }
}

@Composable
private fun TableFilterChip(
    text: String,
    count: Int,
    selected: Boolean,
    statusColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val contentColor = if (selected) colors.primary else colors.onSurfaceVariant
    val borderColor = if (selected) colors.primary else colors.outline.copy(alpha = 0.22f)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) colors.primary.copy(alpha = 0.10f) else Color.Transparent,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(statusColor, CircleShape)
                )
            }

            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            Text(
                text = count.toString(),
                color = contentColor.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ServiceTableCard(
    serviceTable: ServiceTable,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val statusUi = serviceTable.status.toStatusUi()
    val enabled = serviceTable.canOpenTable()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(172.dp)
            .alpha(if (enabled) 1f else 0.78f)
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = 0.86f),
        border = BorderStroke(
            width = 1.dp,
            brush = SolidColor(statusUi.color.copy(alpha = if (enabled) 0.32f else 0.22f))
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                TableIconBadge(
                    color = statusUi.color,
                    iconRes = R.drawable.ic_turntable
                )

                Spacer(modifier = Modifier.weight(1f))

                TableStatusChip(statusUi = statusUi)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Mesa ${serviceTable.number}",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${serviceTable.capacity} lugares",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                color = colors.outline.copy(alpha = 0.16f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (enabled) "Abrir" else "Indisponível",
                    color = if (enabled) statusUi.color else colors.onSurfaceVariant.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                if (enabled) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = statusUi.color,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant.copy(alpha = 0.55f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TableIconBadge(
    color: Color,
    @DrawableRes iconRes: Int
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(color.copy(alpha = 0.12f), CircleShape)
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.58f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(23.dp)
        )
    }
}

@Composable
private fun TableStatusChip(
    statusUi: TableStatusUi
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = statusUi.color.copy(alpha = 0.18f)
    ) {
        Text(
            text = statusUi.label,
            color = statusUi.color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun LoadingTablesState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorTablesState(
    message: String,
    onRetry: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, colors.error.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Não foi possível carregar as mesas",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("Tentar novamente")
            }
        }
    }
}

@Composable
private fun EmptyTablesState() {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = colors.surface.copy(alpha = 0.82f),
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.18f)),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 260.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(colors.primary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_turntable),
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Nenhuma mesa cadastrada",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Cadastre sua primeira mesa para começar a operar.",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun EmptyFilteredTablesState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Nenhuma mesa neste filtro",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private data class TableStatusUi(
    val label: String,
    val color: Color
)

@Composable
private fun ServiceTableStatus.toStatusUi(): TableStatusUi {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    return when (this) {
        ServiceTableStatus.AVAILABLE -> TableStatusUi(
            label = "Livre",
            color = extraColors.success
        )

        ServiceTableStatus.OCCUPIED -> TableStatusUi(
            label = "Ocupada",
            color = extraColors.warning
        )

        ServiceTableStatus.RESERVED -> TableStatusUi(
            label = "Reservada",
            color = colors.secondary
        )

        ServiceTableStatus.INACTIVE -> TableStatusUi(
            label = "Indisponível",
            color = colors.error
        )

        ServiceTableStatus.UNKNOWN -> TableStatusUi(
            label = "Desconhecida",
            color = colors.error
        )
    }
}

private fun ServiceTable.canOpenTable(): Boolean {
    return status == ServiceTableStatus.AVAILABLE ||
            status == ServiceTableStatus.RESERVED ||
            status == ServiceTableStatus.OCCUPIED
}