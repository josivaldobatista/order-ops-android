package com.jfb.orderops.product.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.presentation.state.ProductsUiState

@Composable
fun ProductsScreen(
    uiState: ProductsUiState,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Produtos", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRefresh,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar produtos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(uiState.products) { product ->
                ProductCard(product)
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            product.description?.let {
                Spacer(Modifier.height(4.dp))
                Text(it)
            }
            Spacer(Modifier.height(8.dp))
            Text("Preço: R$ ${"%.2f".format(product.price)}")
            Text(if (product.active) "Ativo" else "Inativo")
        }
    }
}