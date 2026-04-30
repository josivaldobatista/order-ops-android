package com.jfb.orderops.auth.data.dto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val userId: Long,
    val companyId: Long,
    val name: String,
    val email: String,
    val role: String
)