package com.jfb.orderops.product.data.remote

import com.jfb.orderops.product.data.dto.ProductResponse
import retrofit2.http.GET

interface ProductApi {

    @GET("api/v1/products")
    suspend fun list(): List<ProductResponse>
}