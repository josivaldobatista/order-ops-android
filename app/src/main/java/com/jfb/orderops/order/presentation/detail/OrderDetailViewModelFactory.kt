package com.jfb.orderops.order.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jfb.orderops.order.domain.usecase.AddOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.CancelOrderUseCase
import com.jfb.orderops.order.domain.usecase.FinishOrderUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.MarkOrderAsReadyUseCase
import com.jfb.orderops.order.domain.usecase.RemoveOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.SendOrderToPreparationUseCase
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase

class OrderDetailViewModelFactory(
    private val orderId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val sendToPreparationUseCase: SendOrderToPreparationUseCase,
    private val markAsReadyUseCase: MarkOrderAsReadyUseCase,
    private val finishOrderUseCase: FinishOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val addOrderItemUseCase: AddOrderItemUseCase,
    private val removeOrderItemUseCase: RemoveOrderItemUseCase,
    private val listProductsUseCase: ListProductsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderDetailViewModel(
                orderId = orderId,
                getOrderByIdUseCase = getOrderByIdUseCase,
                sendToPreparationUseCase = sendToPreparationUseCase,
                markAsReadyUseCase = markAsReadyUseCase,
                finishOrderUseCase = finishOrderUseCase,
                cancelOrderUseCase = cancelOrderUseCase,
                addOrderItemUseCase = addOrderItemUseCase,
                removeOrderItemUseCase = removeOrderItemUseCase,
                listProductsUseCase = listProductsUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}