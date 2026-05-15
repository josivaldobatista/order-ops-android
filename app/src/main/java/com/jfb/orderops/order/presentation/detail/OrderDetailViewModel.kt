package com.jfb.orderops.order.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.order.domain.usecase.AddOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.AssignOrderItemParticipantUseCase
import com.jfb.orderops.order.domain.usecase.CancelOrderUseCase
import com.jfb.orderops.order.domain.usecase.CreateOrderParticipantUseCase
import com.jfb.orderops.order.domain.usecase.FinishOrderUseCase
import com.jfb.orderops.order.domain.usecase.GetOrderByIdUseCase
import com.jfb.orderops.order.domain.usecase.GetParticipantConsumptionPreviewUseCase
import com.jfb.orderops.order.domain.usecase.ListOrderParticipantsUseCase
import com.jfb.orderops.order.domain.usecase.MarkOrderAsReadyUseCase
import com.jfb.orderops.order.domain.usecase.RemoveOrderItemUseCase
import com.jfb.orderops.order.domain.usecase.SendOrderToPreparationUseCase
import com.jfb.orderops.order.presentation.state.OrderDetailUiState
import com.jfb.orderops.product.domain.usecase.ListProductsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderId: Long,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val sendToPreparationUseCase: SendOrderToPreparationUseCase,
    private val markAsReadyUseCase: MarkOrderAsReadyUseCase,
    private val finishOrderUseCase: FinishOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val addOrderItemUseCase: AddOrderItemUseCase,
    private val removeOrderItemUseCase: RemoveOrderItemUseCase,
    private val listProductsUseCase: ListProductsUseCase,
    private val listOrderParticipantsUseCase: ListOrderParticipantsUseCase,
    private val createOrderParticipantUseCase: CreateOrderParticipantUseCase,
    private val assignOrderItemParticipantUseCase: AssignOrderItemParticipantUseCase,
    private val getParticipantConsumptionPreviewUseCase: GetParticipantConsumptionPreviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun assignItemParticipant(
        itemId: Long,
        participantId: Long?
    ) {
        updateOrder {
            assignOrderItemParticipantUseCase.execute(
                orderId = orderId,
                itemId = itemId,
                participantId = participantId
            )
        }
    }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val orderResult = getOrderByIdUseCase.execute(orderId)

            if (orderResult is AppResult.Error) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = orderResult.message
                    )
                }
                return@launch
            }

            val productsResult = listProductsUseCase.execute()
            val participantsResult = listOrderParticipantsUseCase.execute(orderId)

            val consumptionPreviewResult =
                getParticipantConsumptionPreviewUseCase.execute(orderId)

            when {
                orderResult is AppResult.Success &&
                        productsResult is AppResult.Success &&
                        participantsResult is AppResult.Success &&
                        consumptionPreviewResult is AppResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = orderResult.data,
                            products = productsResult.data,
                            participants = participantsResult.data,
                            consumptionPreview = consumptionPreviewResult.data,
                            errorMessage = null
                        )
                    }
                }

                consumptionPreviewResult is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = consumptionPreviewResult.message
                        )
                    }
                }

                participantsResult is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = participantsResult.message
                        )
                    }
                }

                productsResult is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = productsResult.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun loadConsumptionPreview() {
        viewModelScope.launch {
            when (
                val result = getParticipantConsumptionPreviewUseCase.execute(orderId)
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(consumptionPreview = result.data)
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = result.message)
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }

    fun loadOrder() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = getOrderByIdUseCase.execute(orderId)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = result.data,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }

    fun sendToPreparation() {
        updateOrder {
            sendToPreparationUseCase.execute(orderId)
        }
    }

    fun markAsReady() {
        updateOrder {
            markAsReadyUseCase.execute(orderId)
        }
    }

    fun finish() {
        updateOrder {
            finishOrderUseCase.execute(orderId)
        }
    }

    fun cancel() {
        updateOrder {
            cancelOrderUseCase.execute(orderId)
        }
    }

    fun addItem(productId: Long, quantity: Int) {
        updateOrder {
            addOrderItemUseCase.execute(
                orderId = orderId,
                productId = productId,
                quantity = quantity
            )
        }
    }

    fun removeItem(itemId: Long) {
        updateOrder {
            removeOrderItemUseCase.execute(
                orderId = orderId,
                itemId = itemId
            )
        }
    }

    private fun updateOrder(
        action: suspend () -> AppResult<Order>
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = action()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = result.data,
                            errorMessage = null
                        )
                    }

                    // força reload do pedido (garante consistência)
                    loadOrder()
                    loadConsumptionPreview()

                    _events.emit("Operação realizada com sucesso")
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }

    fun onNewParticipantNameChange(value: String) {
        _uiState.update {
            it.copy(
                newParticipantName = value,
                errorMessage = null
            )
        }
    }

    fun createParticipant() {
        val name = _uiState.value.newParticipantName

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCreatingParticipant = true,
                    errorMessage = null
                )
            }

            when (
                val result = createOrderParticipantUseCase.execute(
                    orderId = orderId,
                    name = name
                )
            ) {
                is AppResult.Success -> {
                    val participantsResult =
                        listOrderParticipantsUseCase.execute(orderId)

                    when (participantsResult) {
                        is AppResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isCreatingParticipant = false,
                                    newParticipantName = "",
                                    participants = participantsResult.data,
                                    errorMessage = null
                                )
                            }

                            _events.emit("Participante adicionado com sucesso")
                        }

                        is AppResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isCreatingParticipant = false,
                                    errorMessage = participantsResult.message
                                )
                            }
                        }

                        AppResult.Loading -> Unit
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreatingParticipant = false,
                            errorMessage = result.message
                        )
                    }
                }

                AppResult.Loading -> Unit
            }
        }
    }
}