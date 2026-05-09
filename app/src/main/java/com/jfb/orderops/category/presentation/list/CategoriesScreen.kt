package com.jfb.orderops.category.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.category.domain.model.Category

@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    onRefresh: () -> Unit,
    onCreateCategory: () -> Unit,
    onBack: () -> Unit
) {
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
            text = "Categorias",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onCreateCategory,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nova categoria")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onRefresh,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar categorias")
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.categories) { category ->
                CategoryCard(category = category)
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}