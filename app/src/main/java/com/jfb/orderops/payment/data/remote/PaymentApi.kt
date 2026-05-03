package com.jfb.orderops.payment.data.remote

import com.jfb.orderops.payment.data.dto.PayOrderRequest
import com.jfb.orderops.payment.data.dto.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {

    @POST("api/v1/orders/{orderId}/payments")
    suspend fun payOrder(
        @Path("orderId") orderId: Long,
        @Body request: PayOrderRequest
    ): PaymentResponse
}