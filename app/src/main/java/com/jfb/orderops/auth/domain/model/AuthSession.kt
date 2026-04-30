package com.jfb.orderops.auth.domain.model

data class AuthSession(
    val accessToken: String,
    val tokenType: String,
    val userId: Long,
    val companyId: Long,
    val name: String,
    val email: String,
    val role: String
)