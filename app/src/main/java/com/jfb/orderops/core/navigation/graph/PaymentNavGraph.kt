package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.payment.data.repository.PaymentRepositoryImpl
import com.jfb.orderops.payment.domain.usecase.PayOrderUseCase
import com.jfb.orderops.payment.presentation.pay.PaymentScreen
import com.jfb.orderops.payment.presentation.pay.PaymentViewModel
import com.jfb.orderops.payment.presentation.pay.PaymentViewModelFactory

fun NavGraphBuilder.paymentGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {

    composable(
        route = AppRoute.Payment.route,
        arguments = listOf(
            navArgument("orderId") {
                type = NavType.LongType
            },
            navArgument("amount") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val orderId =
            backStackEntry.arguments
                ?.getLong("orderId")
                ?: return@composable

        val amount =
            backStackEntry.arguments
                ?.getString("amount")
                ?.toDoubleOrNull()
                ?: 0.0

        val paymentApi =
            RetrofitClient.createPaymentApi(sessionStorage)

        val paymentRepository =
            PaymentRepositoryImpl(paymentApi)

        val payOrderUseCase =
            PayOrderUseCase(paymentRepository)

        val orderApi =
            RetrofitClient.createOrderApi(sessionStorage)

        val orderRepository =
            OrderRepositoryImpl(orderApi)

        val getOrderByIdUseCase =
            GetOrderByIdUseCase(orderRepository)

        val viewModel: PaymentViewModel = viewModel(
            factory = PaymentViewModelFactory(
                orderId = orderId,
                amount = amount,
                payOrderUseCase = payOrderUseCase,
                getOrderByIdUseCase = getOrderByIdUseCase
            )
        )

        val uiState =
            viewModel.uiState.collectAsState().value

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
}