package com.jfb.orderops.auth.domain.usecase

import com.jfb.orderops.auth.domain.model.AuthSession
import com.jfb.orderops.auth.domain.repository.AuthRepository
import com.jfb.orderops.core.result.AppResult

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend fun execute(
        email: String,
        password: String
    ): AppResult<AuthSession> {
        if (email.isBlank()) {
            return AppResult.Error("Email is required.")
        }

        if (password.isBlank()) {
            return AppResult.Error("Password is required.")
        }

        return authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}