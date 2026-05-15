package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jfb.orderops.category.data.repository.CategoryRepositoryImpl
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.order.data.repository.OrderRepositoryImpl
import com.jfb.orderops.order.domain.usecase.AddOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.CancelOrderUseCase
import com.jfb.orderops.order.domain.usecase.CreateOrderParticipantUseCase
import com.jfb.orderops.order.domain.usecase.CreateOrderUseCase
import com.jfb.orderops.order.domain.usecase.FinishOrderUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.ListOrderParticipantsUseCase
import com.jfb.orderops.order.domain.usecase.MarkOrderAsReadyUseCase
import com.jfb.orderops.order.domain.usecase.PreviewPaymentSplitUseCase
import com.jfb.orderops.order.domain.usecase.RemoveOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.SendOrderToPreparationUseCase
import com.jfb.orderops.order.presentation.create.CreateOrderScreen
import com.jfb.orderops.order.presentation.create.CreateOrderViewModel
import com.jfb.orderops.order.presentation.create.CreateOrderViewModelFactory
import com.jfb.orderops.order.presentation.detail.OrderDetailScreen
import com.jfb.orderops.order.presentation.detail.OrderDetailViewModel
import com.jfb.orderops.order.presentation.detail.OrderDetailViewModelFactory
import com.jfb.orderops.product.data.repository.ProductRepositoryImpl
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase

fun NavGraphBuilder.orderGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
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

        val categoryApi = RetrofitClient.createCategoryApi(sessionStorage)
        val categoryRepository = CategoryRepositoryImpl(categoryApi)
        val listCategoriesUseCase = ListCategoriesUseCase(categoryRepository)

        val viewModel: CreateOrderViewModel = viewModel(
            factory = CreateOrderViewModelFactory(
                serviceTableId = serviceTableId,
                listProductsUseCase = listProductsUseCase,
                listCategoriesUseCase = listCategoriesUseCase,
                createOrderUseCase = createOrderUseCase
            )
        )

        val uiState = viewModel.uiState.collectAsState().value

        LaunchedEffect(serviceTableId) {
            viewModel.loadData()
        }

        CreateOrderScreen(
            uiState = uiState,
            onFulfillmentTypeSelected = viewModel::onFulfillmentTypeSelected,
            onCategorySelected = viewModel::onCategorySelected,
            onAddProduct = { productId ->
                viewModel.onProductSelected(productId)
                viewModel.addSelectedProduct()
            },
            onRemoveProduct = viewModel::removeProduct,
            onCreateOrder = {
                viewModel.createOrder(
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

        val listOrderParticipantsUseCase =
            ListOrderParticipantsUseCase(orderRepository)

        val createOrderParticipantUseCase =
            CreateOrderParticipantUseCase(orderRepository)

        val listProductsUseCase = ListProductsUseCase(productRepository)

        val viewModel: OrderDetailViewModel = viewModel(
            factory = OrderDetailViewModelFactory(
                orderId = orderId,
                getOrderByIdUseCase = getOrderByIdUseCase,
                sendToPreparationUseCase = sendToPreparationUseCase,
                markAsReadyUseCase = markAsReadyUseCase,
                finishOrderUseCase = finishOrderUseCase,
                cancelOrderUseCase = cancelOrderUseCase,
                addOrderItemUseCase = addOrderItemUseCase,
                removeOrderItemUseCase = removeOrderItemUseCase,
                listProductsUseCase = listProductsUseCase,
                listOrderParticipantsUseCase = listOrderParticipantsUseCase,
                createOrderParticipantUseCase = createOrderParticipantUseCase,
            )
        )

        val uiState = viewModel.uiState.collectAsState().value

        LaunchedEffect(orderId) {
            viewModel.loadAll()
        }

        OrderDetailScreen(
            uiState = uiState,
            onRefresh = viewModel::loadAll,
            onNewParticipantNameChange = viewModel::onNewParticipantNameChange,
            onCreateParticipant = viewModel::createParticipant,
            onBack = {
                navController.popBackStack()
            },
            onSendToPreparation = viewModel::sendToPreparation,
            onMarkAsReady = viewModel::markAsReady,
            events = viewModel.events,
            onFinish = viewModel::finish,
            onCancel = viewModel::cancel,
            onGoToPayment = { id, amount ->
                navController.navigate(
                    AppRoute.Payment.createRoute(id, amount)
                )
            },
            onAddItem = { productId, qty ->
                viewModel.addItem(productId, qty)
            },
            onRemoveItem = { itemId ->
                viewModel.removeItem(itemId)
            }
        )
    }
}