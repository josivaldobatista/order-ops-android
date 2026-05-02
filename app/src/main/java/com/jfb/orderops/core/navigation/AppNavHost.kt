package com.jfb.orderops.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jfb.orderops.auth.data.repository.AuthRepositoryImpl
import com.jfb.orderops.auth.domain.usecase.LoginUseCase
import com.jfb.orderops.auth.presentation.login.LoginScreen
import com.jfb.orderops.auth.presentation.login.LoginViewModel
import com.jfb.orderops.auth.presentation.login.LoginViewModelFactory
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.dashboard.presentation.DashboardScreen
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.order.presentation.create.CreateOrderScreen
import com.jfb.orderops.order.presentation.create.CreateOrderViewModel
import com.jfb.orderops.order.presentation.create.CreateOrderViewModelFactory
import com.jfb.orderops.product.data.repository.ProductRepositoryImpl
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    val authApi = RetrofitClient.createAuthApi(sessionStorage)

    val repository = AuthRepositoryImpl(
        api = authApi,
        sessionStorage = sessionStorage
    )

    val useCase = LoginUseCase(repository)

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(useCase)
    )

    val uiState = loginViewModel.uiState.collectAsState().value

    val startDestination = if (sessionStorage.isLoggedIn()) {
        AppRoute.Dashboard.route
    } else {
        AppRoute.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = AppRoute.CreateOrder.route,
            arguments = listOf(
                navArgument("serviceTableId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val serviceTableId = backStackEntry.arguments
                ?.getLong("serviceTableId")
                ?: return@composable

            val orderApi = RetrofitClient.createOrderApi(sessionStorage)
            val orderRepository = OrderRepositoryImpl(orderApi)
            val createOrderUseCase = CreateOrderUseCase(orderRepository)

            val productApi = RetrofitClient.createProductApi(sessionStorage)
            val productRepository = ProductRepositoryImpl(productApi)
            val listProductsUseCase = ListProductsUseCase(productRepository)

            val createOrderViewModel: CreateOrderViewModel = viewModel(
                factory = CreateOrderViewModelFactory(
                    serviceTableId = serviceTableId,
                    listProductsUseCase = listProductsUseCase,
                    createOrderUseCase = createOrderUseCase
                )
            )

            val uiState = createOrderViewModel.uiState.collectAsState().value

            LaunchedEffect(Unit) {
                createOrderViewModel.loadProducts()
            }

            CreateOrderScreen(
                uiState = uiState,
                onProductSelected = createOrderViewModel::onProductSelected,
                onIncreaseQuantity = createOrderViewModel::increaseQuantity,
                onDecreaseQuantity = createOrderViewModel::decreaseQuantity,
                onAddProduct = createOrderViewModel::addSelectedProduct,
                onRemoveProduct = createOrderViewModel::removeProduct,
                onCreateOrder = {
                    createOrderViewModel.createOrder(
                        onSuccess = { orderId ->
                            navController.navigate(
                                AppRoute.OrderDetail.createRoute(orderId)
                            ) {
                                popUpTo(AppRoute.CreateOrder.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.Login.route) {
            LoginScreen(
                uiState = uiState,
                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onLoginClick = loginViewModel::login
            )

            if (uiState.isLoggedIn) {
                navController.navigate(AppRoute.Dashboard.route) {
                    popUpTo(AppRoute.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(AppRoute.Dashboard.route) {
            DashboardScreen(
                sessionStorage = sessionStorage,
                navController = navController,
                onLogout = {
                    sessionStorage.clear()

                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.Dashboard.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = AppRoute.OrderDetail.route,
            arguments = listOf(
                navArgument("orderId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments
                ?.getLong("orderId")
                ?: return@composable

            Text("Pedido ID: $orderId")
        }
    }
}