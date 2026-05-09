package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jfb.orderops.company.data.repository.CompanyRepositoryImpl
import com.jfb.orderops.company.domain.usecase.GetCompanyByIdUseCase
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.core.util.ShareImageUtils
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.payment.domain.model.PaymentMethod
import com.jfb.orderops.receipt.presentation.ReceiptBitmapRenderer
import com.jfb.orderops.receipt.presentation.ReceiptScreen
import com.jfb.orderops.receipt.presentation.ReceiptViewModel
import com.jfb.orderops.receipt.presentation.ReceiptViewModelFactory

fun NavGraphBuilder.receiptGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    composable(
        route = AppRoute.Receipt.route,
        arguments = listOf(
            navArgument("orderId") {
                type = NavType.LongType
            },
            navArgument("method") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val orderId = backStackEntry.arguments
            ?.getLong("orderId")
            ?: return@composable

        val methodName = backStackEntry.arguments
            ?.getString("method")
            ?: PaymentMethod.PIX.name

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

        val viewModel: ReceiptViewModel = viewModel(
            factory = ReceiptViewModelFactory(
                orderId = orderId,
                companyId = companyId,
                getOrderByIdUseCase = getOrderByIdUseCase,
                getCompanyByIdUseCase = getCompanyByIdUseCase
            )
        )

        val uiState = viewModel.uiState.collectAsState().value

        LaunchedEffect(orderId) {
            viewModel.load()
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