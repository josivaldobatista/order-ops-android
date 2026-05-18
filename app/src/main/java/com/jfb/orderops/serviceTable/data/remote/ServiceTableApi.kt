package com.jfb.orderops.serviceTable.data.remote

import com.jfb.orderops.serviceTable.data.dto.CreateServiceTableRequest
import com.jfb.orderops.serviceTable.data.dto.ServiceTableResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceTableApi {

    @GET("api/v1/service-tables")
    suspend fun list(): List<ServiceTableResponse>

    @POST("api/v1/service-tables")
    suspend fun create(
        @Body request: CreateServiceTableRequest
    ): ServiceTableResponse

    @PATCH("api/v1/service-tables/{id}/reserve")
    suspend fun reserve(
        @Path("id") id: Long
    ): ServiceTableResponse

    @PATCH("api/v1/service-tables/{id}/occupy")
    suspend fun occupy(
        @Path("id") id: Long
    ): ServiceTableResponse

    @PATCH("api/v1/service-tables/{id}/release")
    suspend fun release(
        @Path("id") id: Long
    ): ServiceTableResponse

    @PATCH("api/v1/service-tables/{id}/deactivate")
    suspend fun deactivate(
        @Path("id") id: Long
    )
}