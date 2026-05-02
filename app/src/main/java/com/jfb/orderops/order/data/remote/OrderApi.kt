package com.jfb.orderops.order.data.remote

import com.jfb.orderops.order.data.dto.CreateOrderRequest
import com.jfb.orderops.order.data.dto.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
}