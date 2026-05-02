package com.jfb.orderops.order.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.product.domain.model.Product

@Composable
fun OrderDetailScreen(
    order: Order,
    products: List<Product>,
    onAddProduct: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pedido #${order.id}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Itens:")

        order.items.forEach {
            Text("- ${it.quantity}x ${it.productName}")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Adicionar produtos:")

        Spacer(modifier = Modifier.height(8.dp))

        products.forEach { product ->
            Button(
                onClick = { onAddProduct(product.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${product.name} - R$ ${"%.2f".format(product.price)}")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}