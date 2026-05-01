package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.ListOrdersUseCase
import com.jfb.orderops.order.presentation.list.OrdersScreen
import com.jfb.orderops.order.presentation.list.OrdersViewModel
import com.jfb.orderops.order.presentation.list.OrdersViewModelFactory
import com.jfb.orderops.product.data.repository.ProductRepositoryImpl
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import com.jfb.orderops.product.presentation.list.ProductsScreen
import com.jfb.orderops.product.presentation.list.ProductsViewModel
import com.jfb.orderops.product.presentation.list.ProductsViewModelFactory
import com.jfb.orderops.serviceTable.data.repository.ServiceTableRepositoryImpl
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesScreen
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesViewModel
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesViewModelFactory

@Composable
fun DashboardScreen(
    sessionStorage: SessionStorage,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val serviceTableApi = RetrofitClient.createServiceTableApi(sessionStorage)
    val serviceTableRepository = ServiceTableRepositoryImpl(serviceTableApi)
    val listServiceTablesUseCase = ListServiceTablesUseCase(serviceTableRepository)

    val serviceTablesViewModel: ServiceTablesViewModel = viewModel(
        factory = ServiceTablesViewModelFactory(listServiceTablesUseCase)
    )
    val serviceTablesUiState = serviceTablesViewModel.uiState.collectAsState().value

    val orderApi = RetrofitClient.createOrderApi(sessionStorage)
    val orderRepository = OrderRepositoryImpl(orderApi)
    val listOrdersUseCase = ListOrdersUseCase(orderRepository)

    val ordersViewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(listOrdersUseCase)
    )
    val ordersUiState = ordersViewModel.uiState.collectAsState().value

    val productApi = RetrofitClient.createProductApi(sessionStorage)
    val productRepository = ProductRepositoryImpl(productApi)
    val listProductsUseCase = ListProductsUseCase(productRepository)

    val productsViewModel: ProductsViewModel = viewModel(
        factory = ProductsViewModelFactory(listProductsUseCase)
    )
    val productsUiState = productsViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        serviceTablesViewModel.loadServiceTables()
        ordersViewModel.loadOrders()
        productsViewModel.loadProducts()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "OrderOps",
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = onLogout) {
                Text("Logout")
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Mesas") }
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Pedidos") }
            )

            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Produtos") }
            )
        }

        when (selectedTab) {
            0 -> ServiceTablesScreen(
                uiState = serviceTablesUiState,
                onRefresh = serviceTablesViewModel::loadServiceTables
            )

            1 -> OrdersScreen(
                uiState = ordersUiState,
                onRefresh = { ordersViewModel.loadOrders() },
                onStatusSelected = { status ->
                    ordersViewModel.loadOrders(status)
                }
            )

            2 -> ProductsScreen(
                uiState = productsUiState,
                onRefresh = productsViewModel::loadProducts
            )
        }
    }
}