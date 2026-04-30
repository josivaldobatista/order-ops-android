package com.jfb.orderops.auth.data.dto

data class LoginRequest(
    val email: String,
    val password: String
)