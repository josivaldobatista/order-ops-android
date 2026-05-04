package com.jfb.orderops.serviceTable.data.remote

import com.jfb.orderops.serviceTable.data.dto.CreateServiceTableRequest
import com.jfb.orderops.serviceTable.data.dto.ServiceTableResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServiceTableApi {

    @GET("api/v1/service-tables")
    suspend fun list(): List<ServiceTableResponse>

    @POST("api/v1/service-tables")
    suspend fun create(
        @Body request: CreateServiceTableRequest
    ): ServiceTableResponse
}