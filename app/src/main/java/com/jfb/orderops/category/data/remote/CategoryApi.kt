package com.jfb.orderops.category.data.remote

import com.jfb.orderops.category.data.dto.CategoryResponse
import com.jfb.orderops.category.data.dto.CreateCategoryRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CategoryApi {

    @GET("api/v1/categories")
    suspend fun list(): List<CategoryResponse>


    @POST("api/v1/categories")
    suspend fun create(
        @Body request: CreateCategoryRequest
    ): CategoryResponse

}