package com.jfb.orderops.payment.data.remote

import com.jfb.orderops.payment.data.dto.DailyPaymentReportResponse
import com.jfb.orderops.payment.data.dto.PayOrderRequest
import com.jfb.orderops.payment.data.dto.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

import com.jfb.orderops.payment.data.dto.PaymentReportResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentApi {

    @POST("api/v1/orders/{orderId}/payments")
    suspend fun payOrder(
        @Path("orderId") orderId: Long,
        @Body request: PayOrderRequest
    ): PaymentResponse

    @GET("api/v1/payments/report")
    suspend fun getReport(
        @Query("start") start: String,
        @Query("end") end: String
    ): PaymentReportResponse

    @GET("api/v1/payments/ticket-average")
    suspend fun getTicketAverage(
        @Query("start") start: String,
        @Query("end") end: String
    ): Double

    @GET("api/v1/payments/daily-report")
    suspend fun getDailyReport(
        @Query("start") start: String,
        @Query("end") end: String
    ): List<DailyPaymentReportResponse>
}