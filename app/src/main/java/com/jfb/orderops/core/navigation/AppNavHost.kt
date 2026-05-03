package com.jfb.orderops.core.navigation

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
import com.jfb.orderops.order.domain.usecase.AddOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.CancelOrderUseCase
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.order.domain.usecase.FinishOrderUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.MarkOrderAsReadyUseCase
import com.jfb.orderops.order.domain.usecase.RemoveOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.SendOrderToPreparationUseCase
import com.jfb.orderops.order.presentation.create.CreateOrderScreen
import com.jfb.orderops.order.presentation.create.CreateOrderViewModel
import com.jfb.orderops.order.presentation.create.CreateOrderViewModelFactory
import com.jfb.orderops.order.presentation.detail.OrderDetailScreen
import com.jfb.orderops.order.presentation.detail.OrderDetailViewModel
import com.jfb.orderops.order.presentation.detail.OrderDetailViewModelFactory
import com.jfb.orderops.payment.data.repository.PaymentRepositoryImpl
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase
import com.jfb.orderops.payment.presentation.pay.PaymentScreen
import com.jfb.orderops.payment.presentation.pay.PaymentViewModel
import com.jfb.orderops.payment.presentation.pay.PaymentViewModelFactory
import com.jfb.orderops.product.data.repository.ProductRepositoryImpl
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    val authApi = RetrofitClient.createAuthApi(sessionStorage)

    val authRepository = AuthRepositoryImpl(
        api = authApi,
        sessionStorage = sessionStorage
    )

    val loginUseCase = LoginUseCase(authRepository)

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(loginUseCase)
    )

    val loginUiState = loginViewModel.uiState.collectAsState().value

    val startDestination = if (sessionStorage.isLoggedIn()) {
        AppRoute.Dashboard.route
    } else {
        AppRoute.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                uiState = loginUiState,
                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onLoginClick = loginViewModel::login
            )

            if (loginUiState.isLoggedIn) {
                navController.navigate(AppRoute.Dashboard.route) {
                    popUpTo(AppRoute.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(
            route = AppRoute.Payment.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.LongType },
                navArgument("amount") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val orderId = backStackEntry.arguments?.getLong("orderId") ?: return@composable
            val amount = backStackEntry.arguments
                ?.getString("amount")
                ?.toDoubleOrNull()
                ?: 0.0

            val paymentApi = RetrofitClient.createPaymentApi(sessionStorage)
            val paymentRepository = PaymentRepositoryImpl(paymentApi)
            val payOrderUseCase = PayOrderUseCase(paymentRepository)

            val viewModel: PaymentViewModel = viewModel(
                factory = PaymentViewModelFactory(
                    orderId = orderId,
                    amount = amount,
                    payOrderUseCase = payOrderUseCase
                )
            )

            val uiState = viewModel.uiState.collectAsState().value

            PaymentScreen(
                uiState = uiState,
                onMethodSelected = viewModel::onMethodSelected,
                onPayClick = {
                    viewModel.pay {
                        navController.navigate(AppRoute.Dashboard.route) {
                            popUpTo(AppRoute.Dashboard.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
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

            val createOrderUiState = createOrderViewModel.uiState.collectAsState().value

            LaunchedEffect(serviceTableId) {
                createOrderViewModel.loadProducts()
            }

            CreateOrderScreen(
                uiState = createOrderUiState,
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

            val orderApi = RetrofitClient.createOrderApi(sessionStorage)
            val orderRepository = OrderRepositoryImpl(orderApi)

            val productApi = RetrofitClient.createProductApi(sessionStorage)
            val productRepository = ProductRepositoryImpl(productApi)

            val getOrderByIdUseCase = GetOrderByIdUseCase(orderRepository)
            val sendToPreparationUseCase = SendOrderToPreparationUseCase(orderRepository)
            val markAsReadyUseCase = MarkOrderAsReadyUseCase(orderRepository)
            val finishOrderUseCase = FinishOrderUseCase(orderRepository)
            val cancelOrderUseCase = CancelOrderUseCase(orderRepository)

            val addOrderItemUseCase = AddOrderItemUseCase(orderRepository)
            val removeOrderItemUseCase = RemoveOrderItemUseCase(orderRepository)

            val listProductsUseCase = ListProductsUseCase(productRepository)

            val orderDetailViewModel: OrderDetailViewModel = viewModel(
                factory = OrderDetailViewModelFactory(
                    orderId = orderId,
                    getOrderByIdUseCase = getOrderByIdUseCase,
                    sendToPreparationUseCase = sendToPreparationUseCase,
                    markAsReadyUseCase = markAsReadyUseCase,
                    finishOrderUseCase = finishOrderUseCase,
                    cancelOrderUseCase = cancelOrderUseCase,
                    addOrderItemUseCase = addOrderItemUseCase,
                    removeOrderItemUseCase = removeOrderItemUseCase,
                    listProductsUseCase = listProductsUseCase
                )
            )

            val uiState = orderDetailViewModel.uiState.collectAsState().value

            LaunchedEffect(orderId) {
                orderDetailViewModel.loadAll()
            }

            OrderDetailScreen(
                uiState = uiState,
                onRefresh = orderDetailViewModel::loadAll,
                onBack = { navController.popBackStack() },
                onSendToPreparation = orderDetailViewModel::sendToPreparation,
                onMarkAsReady = orderDetailViewModel::markAsReady,
                events = orderDetailViewModel.events,
                onFinish = orderDetailViewModel::finish,
                onCancel = orderDetailViewModel::cancel,
                onGoToPayment = { orderId, amount ->
                    navController.navigate(
                        AppRoute.Payment.createRoute(orderId, amount)
                    )
                },
                onAddItem = { productId, qty ->
                    orderDetailViewModel.addItem(productId, qty)
                },
                onRemoveItem = { itemId ->
                    orderDetailViewModel.removeItem(itemId)
                }
            )
        }
    }
}