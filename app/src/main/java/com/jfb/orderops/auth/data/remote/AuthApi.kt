package com.jfb.orderops.auth.data.remote

import com.jfb.orderops.auth.data.dto.AuthResponse
import com.jfb.orderops.auth.data.dto.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse
}