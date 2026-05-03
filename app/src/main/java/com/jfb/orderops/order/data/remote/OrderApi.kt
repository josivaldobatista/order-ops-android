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
import retrofit2.http.DELETE

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
}