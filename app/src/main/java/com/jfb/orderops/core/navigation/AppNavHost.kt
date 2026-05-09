package com.jfb.orderops.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
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
import com.jfb.orderops.company.data.repository.CompanyRepositoryImpl
import com.jfb.orderops.company.domain.usecase.GetCompanyByIdUseCase
import com.jfb.orderops.core.auth.AuthSessionEvent
import com.jfb.orderops.core.auth.AuthSessionEventBus
import com.jfb.orderops.core.navigation.graph.categoryGraph
import com.jfb.orderops.core.navigation.graph.orderGraph
import com.jfb.orderops.core.navigation.graph.productGraph
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.core.util.ShareImageUtils
import com.jfb.orderops.dashboard.presentation.DashboardScreen
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.payment.data.repository.PaymentRepositoryImpl
import com.jfb.orderops.payment.domain.model.PaymentMethod
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase
import com.jfb.orderops.payment.presentation.pay.PaymentScreen
import com.jfb.orderops.payment.presentation.pay.PaymentViewModel
import com.jfb.orderops.payment.presentation.pay.PaymentViewModelFactory
import com.jfb.orderops.receipt.presentation.ReceiptBitmapRenderer
import com.jfb.orderops.receipt.presentation.ReceiptScreen
import com.jfb.orderops.receipt.presentation.ReceiptViewModel
import com.jfb.orderops.receipt.presentation.ReceiptViewModelFactory
import com.jfb.orderops.serviceTable.data.repository.ServiceTableRepositoryImpl
import com.jfb.orderops.serviceTable.domain.usecase.CreateServiceTableUseCase
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableScreen
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableViewModel
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableViewModelFactory

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

    LaunchedEffect(Unit) {
        AuthSessionEventBus.events.collect { event ->
            when (event) {
                AuthSessionEvent.SessionExpired -> {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
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

        productGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        categoryGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        orderGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        composable(AppRoute.CreateServiceTable.route) {
            val serviceTableApi = RetrofitClient.createServiceTableApi(sessionStorage)
            val repository = ServiceTableRepositoryImpl(serviceTableApi)
            val useCase = CreateServiceTableUseCase(repository)

            val viewModel: CreateServiceTableViewModel = viewModel(
                factory = CreateServiceTableViewModelFactory(useCase)
            )

            val uiState = viewModel.uiState.collectAsState().value

            CreateServiceTableScreen(
                uiState = uiState,
                onNumberChange = viewModel::onNumberChange,
                onCapacityChange = viewModel::onCapacityChange,
                onCreate = {
                    viewModel.create {
                        navController.popBackStack()
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
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

            val orderApi = RetrofitClient.createOrderApi(sessionStorage)
            val orderRepository = OrderRepositoryImpl(orderApi)
            val getOrderByIdUseCase = GetOrderByIdUseCase(orderRepository)

            val viewModel: PaymentViewModel = viewModel(
                factory = PaymentViewModelFactory(
                    orderId = orderId,
                    amount = amount,
                    payOrderUseCase = payOrderUseCase,
                    getOrderByIdUseCase = getOrderByIdUseCase
                )
            )

            val uiState = viewModel.uiState.collectAsState().value

            LaunchedEffect(orderId) {
                viewModel.loadOrder()
            }

            PaymentScreen(
                uiState = uiState,
                onMethodSelected = viewModel::onMethodSelected,
                onPayClick = {
                    viewModel.pay {
                        navController.navigate(
                            AppRoute.Receipt.createRoute(
                                orderId = uiState.orderId,
                                method = uiState.selectedMethod.name
                            )
                        ) {
                            popUpTo(AppRoute.Payment.route) {
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

        composable(
            route = AppRoute.Receipt.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.LongType },
                navArgument("method") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: return@composable
            val methodName = backStackEntry.arguments?.getString("method") ?: PaymentMethod.PIX.name

            val paymentMethod = runCatching {
                PaymentMethod.valueOf(methodName)
            }.getOrDefault(PaymentMethod.PIX)

            val context = LocalContext.current
            val companyId = sessionStorage.getCompanyId()

            val orderApi = RetrofitClient.createOrderApi(sessionStorage)
            val orderRepository = OrderRepositoryImpl(orderApi)
            val getOrderByIdUseCase = GetOrderByIdUseCase(orderRepository)

            val companyApi = RetrofitClient.createCompanyApi(sessionStorage)
            val companyRepository = CompanyRepositoryImpl(companyApi)
            val getCompanyByIdUseCase = GetCompanyByIdUseCase(companyRepository)

            val receiptViewModel: ReceiptViewModel = viewModel(
                factory = ReceiptViewModelFactory(
                    orderId = orderId,
                    companyId = companyId,
                    getOrderByIdUseCase = getOrderByIdUseCase,
                    getCompanyByIdUseCase = getCompanyByIdUseCase
                )
            )

            val uiState = receiptViewModel.uiState.collectAsState().value

            LaunchedEffect(orderId) {
                receiptViewModel.load()
            }

            val order = uiState.order
            val company = uiState.company

            if (order == null || company == null) {
                val loadingBitmap = ReceiptBitmapRenderer.renderLoading()

                ReceiptScreen(
                    receiptBitmap = loadingBitmap,
                    onShare = {},
                    onClose = {
                        navController.navigate(AppRoute.Dashboard.route) {
                            popUpTo(AppRoute.Dashboard.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            } else {
                val addressText = company.address?.let { address ->
                    "${address.street}, ${address.number} - ${address.city}/${address.state}"
                } ?: "Endereço não informado"

                val receiptBitmap = ReceiptBitmapRenderer.render(
                    order = order,
                    paymentMethod = paymentMethod,
                    companyName = company.name,
                    document = company.document,
                    address = addressText
                )

                ReceiptScreen(
                    receiptBitmap = receiptBitmap,
                    onShare = {
                        ShareImageUtils.shareBitmap(
                            context = context,
                            bitmap = receiptBitmap
                        )
                    },
                    onClose = {
                        navController.navigate(AppRoute.Dashboard.route) {
                            popUpTo(AppRoute.Dashboard.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}