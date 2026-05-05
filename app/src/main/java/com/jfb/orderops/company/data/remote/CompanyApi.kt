package com.jfb.orderops.company.data.remote

import com.jfb.orderops.company.data.dto.CompanyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyApi {

    @GET("api/v1/companies/{id}")
    suspend fun getById(
        @Path("id") id: Long
    ): CompanyResponse
}