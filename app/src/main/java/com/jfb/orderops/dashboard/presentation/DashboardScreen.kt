package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.ListOrdersUseCase
import com.jfb.orderops.order.presentation.list.OrdersScreen
import com.jfb.orderops.order.presentation.list.OrdersViewModel
import com.jfb.orderops.order.presentation.list.OrdersViewModelFactory
import com.jfb.orderops.payment.data.repository.PaymentRepositoryImpl
import com.jfb.orderops.payment.domain.usecase.GetDailyPaymentReportUseCase
import com.jfb.orderops.payment.domain.usecase.GetPaymentReportUseCase
import com.jfb.orderops.payment.presentation.report.PaymentReportScreen
import com.jfb.orderops.payment.presentation.report.PaymentReportViewModel
import com.jfb.orderops.payment.presentation.report.PaymentReportViewModelFactory
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
    navController: NavController,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

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

    val paymentApi = RetrofitClient.createPaymentApi(sessionStorage)
    val paymentRepository = PaymentRepositoryImpl(paymentApi)
    val getReportUseCase = GetPaymentReportUseCase(paymentRepository)
    val getDailyUseCase = GetDailyPaymentReportUseCase(paymentRepository)

    val reportViewModel: PaymentReportViewModel = viewModel(
        factory = PaymentReportViewModelFactory(
            reportUseCase = getReportUseCase,
            dailyUseCase = getDailyUseCase
        )
    )
    val reportUiState = reportViewModel.uiState.collectAsState().value

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            serviceTablesViewModel.loadServiceTables()
            ordersViewModel.loadOrders()
            productsViewModel.loadProducts()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("OrderOps", style = MaterialTheme.typography.titleLarge)

            Button(onClick = onLogout) {
                Text("Logout")
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            ) {
                Text("Mesas")
            }

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            ) {
                Text("Pedidos")
            }

            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 }
            ) {
                Text("Produtos")
            }

            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 }
            ) {
                Text("Relatórios")
            }
        }

        when (selectedTab) {
            0 -> Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        navController.navigate(AppRoute.CreateServiceTable.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Nova mesa")
                }

                ServiceTablesScreen(
                    uiState = serviceTablesUiState,
                    onRefresh = serviceTablesViewModel::loadServiceTables,
                    onTableClick = { tableId ->
                        navController.navigate(
                            AppRoute.CreateOrder.createRoute(tableId)
                        )
                    }
                )
            }

            1 -> OrdersScreen(
                uiState = ordersUiState,
                onRefresh = { ordersViewModel.loadOrders() },
                onStatusSelected = { status ->
                    ordersViewModel.loadOrders(status)
                },
                onOrderClick = { orderId ->
                    navController.navigate(
                        AppRoute.OrderDetail.createRoute(orderId)
                    )
                }
            )

            2 -> Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        navController.navigate(AppRoute.CreateProduct.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Novo produto")
                }

                ProductsScreen(
                    uiState = productsUiState,
                    onRefresh = productsViewModel::loadProducts
                )
            }

            3 -> PaymentReportScreen(
                uiState = reportUiState,
                onLoad = reportViewModel::loadToday
            )
        }
    }
}