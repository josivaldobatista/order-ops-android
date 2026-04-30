package com.jfb.orderops.auth.data.dto

import com.jfb.orderops.auth.domain.model.AuthSession

fun AuthResponse.toDomain(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        tokenType = tokenType,
        userId = userId,
        companyId = companyId,
        name = name,
        email = email,
        role = role
    )
}