package com.jfb.orderops.category.data.remote

import com.jfb.orderops.category.data.dto.CategoryResponse
import com.jfb.orderops.category.data.dto.CreateCategoryRequest
import com.jfb.orderops.category.data.dto.UpdateCategoryRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApi {

    @GET("api/v1/categories")
    suspend fun list(): List<CategoryResponse>


    @POST("api/v1/categories")
    suspend fun create(
        @Body request: CreateCategoryRequest
    ): CategoryResponse

    @PUT("api/v1/categories/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: UpdateCategoryRequest
    ): CategoryResponse

}