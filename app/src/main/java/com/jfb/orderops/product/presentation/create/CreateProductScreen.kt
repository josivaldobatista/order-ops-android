package com.jfb.orderops.product.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    uiState: CreateProductUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategorySelected: (Long) -> Unit,
    onCreate: () -> Unit,
    onBack: () -> Unit
) {

    var isCategoryMenuExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            text = "Novo produto",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = isCategoryMenuExpanded,
            onExpandedChange = {
                isCategoryMenuExpanded = !isCategoryMenuExpanded
            }
        ) {
            val selectedCategory = uiState.categories
                .find { it.id == uiState.selectedCategoryId }

            OutlinedTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoria") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isCategoryMenuExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            ExposedDropdownMenu(
                expanded = isCategoryMenuExpanded,
                onDismissRequest = {
                    isCategoryMenuExpanded = false
                }
            ) {
                uiState.categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(category.name)
                        },
                        onClick = {
                            onCategorySelected(category.id)
                            isCategoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.price,
            onValueChange = onPriceChange,
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(16.dp))

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = onCreate,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar produto")
        }

        if (uiState.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}