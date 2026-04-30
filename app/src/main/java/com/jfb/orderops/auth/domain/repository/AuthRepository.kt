package com.jfb.orderops.auth.domain.repository

import com.jfb.orderops.auth.domain.model.AuthSession
import com.jfb.orderops.core.result.AppResult

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): AppResult<AuthSession>
}