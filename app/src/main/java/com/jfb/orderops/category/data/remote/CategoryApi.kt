package com.jfb.orderops.category.data.remote

import com.jfb.orderops.category.data.dto.CategoryResponse
import retrofit2.http.GET

interface CategoryApi {

    @GET("api/v1/categories")
    suspend fun list(): List<CategoryResponse>
}