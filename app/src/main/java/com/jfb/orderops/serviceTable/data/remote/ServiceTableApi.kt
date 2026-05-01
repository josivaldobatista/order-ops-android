package com.jfb.orderops.serviceTable.data.remote

import com.jfb.orderops.serviceTable.data.dto.ServiceTableResponse
import retrofit2.http.GET

interface ServiceTableApi {

    @GET("api/v1/service-tables")
    suspend fun list(): List<ServiceTableResponse>
}