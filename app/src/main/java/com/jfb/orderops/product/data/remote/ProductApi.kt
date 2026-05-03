package com.jfb.orderops.product.data.remote

import com.jfb.orderops.product.data.dto.CreateProductRequest
import com.jfb.orderops.product.data.dto.ProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    @GET("api/v1/products")
    suspend fun list(): List<ProductResponse>

    @POST("api/v1/products")
    suspend fun create(
        @Body request: CreateProductRequest
    ): ProductResponse

}