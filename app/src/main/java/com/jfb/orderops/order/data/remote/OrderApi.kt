package com.jfb.orderops.order.data.remote

import com.jfb.orderops.order.data.dto.CreateOrderRequest
import com.jfb.orderops.order.data.dto.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.jfb.orderops.order.data.dto.AddOrderItemRequest
import com.jfb.orderops.order.data.dto.AssignOrderItemParticipantRequest
import com.jfb.orderops.order.data.dto.CreateOrderParticipantRequest
import com.jfb.orderops.order.data.dto.OrderParticipantConsumptionPreviewResponse
import com.jfb.orderops.order.data.dto.OrderParticipantResponse
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewRequest
import com.jfb.orderops.order.data.dto.PaymentSplitPreviewResponse
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface OrderApi {

    @GET("api/v1/orders")
    suspend fun list(
        @Query("status") status: String? = null
    ): List<OrderResponse>

    @POST("api/v1/orders")
    suspend fun create(
        @Body request: CreateOrderRequest
    ): OrderResponse

    @GET("api/v1/orders/{id}")
    suspend fun getById(
        @Path("id") id: Long
    ): OrderResponse

    @PATCH("api/v1/orders/{id}/send-to-preparation")
    suspend fun sendToPreparation(
        @Path("id") id: Long
    ): OrderResponse

    @PATCH("api/v1/orders/{id}/ready")
    suspend fun markAsReady(
        @Path("id") id: Long
    ): OrderResponse

    @PATCH("api/v1/orders/{id}/finish")
    suspend fun finish(
        @Path("id") id: Long
    ): OrderResponse

    @PATCH("api/v1/orders/{id}/cancel")
    suspend fun cancel(
        @Path("id") id: Long
    ): OrderResponse

    @POST("api/v1/orders/{id}/items")
    suspend fun addItem(
        @Path("id") orderId: Long,
        @Body request: AddOrderItemRequest
    ): OrderResponse

    @DELETE("api/v1/orders/{orderId}/items/{itemId}")
    suspend fun removeItem(
        @Path("orderId") orderId: Long,
        @Path("itemId") itemId: Long
    ): OrderResponse

    @POST("api/v1/orders/{orderId}/payment-splits/preview")
    suspend fun previewPaymentSplit(
        @Path("orderId") orderId: Long,
        @Body request: PaymentSplitPreviewRequest
    ): PaymentSplitPreviewResponse

    @GET("api/v1/orders/{orderId}/participants")
    suspend fun listParticipants(
        @Path("orderId") orderId: Long
    ): List<OrderParticipantResponse>

    @POST("api/v1/orders/{orderId}/participants")
    suspend fun createParticipant(
        @Path("orderId") orderId: Long,
        @Body request: CreateOrderParticipantRequest
    ): OrderParticipantResponse

    @PUT("api/v1/orders/{orderId}/items/{itemId}/participant")
    suspend fun assignItemParticipant(
        @Path("orderId") orderId: Long,
        @Path("itemId") itemId: Long,
        @Body request: AssignOrderItemParticipantRequest
    ): OrderResponse

    @GET("api/v1/orders/{orderId}/participants/consumption-preview")
    suspend fun getParticipantConsumptionPreview(
        @Path("orderId") orderId: Long
    ): OrderParticipantConsumptionPreviewResponse
}