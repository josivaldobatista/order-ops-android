package com.jfb.orderops.auth.data.repository

import com.jfb.orderops.auth.data.dto.LoginRequest
import com.jfb.orderops.auth.data.dto.toDomain
import com.jfb.orderops.auth.data.remote.AuthApi
import com.jfb.orderops.auth.domain.model.AuthSession
import com.jfb.orderops.auth.domain.repository.AuthRepository
import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.core.storage.SessionStorage

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): AppResult<AuthSession> {
        return try {
            val response = api.login(
                LoginRequest(email, password)
            )

            val session = response.toDomain()

            sessionStorage.save(session)

            AppResult.Success(session)

        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Unknown error",
                throwable = e
            )
        }
    }
}