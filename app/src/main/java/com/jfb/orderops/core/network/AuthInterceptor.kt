package com.jfb.orderops.core.network

import com.jfb.orderops.core.auth.AuthSessionEventBus
import com.jfb.orderops.core.storage.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionStorage: SessionStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionStorage.getAccessToken()

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 || response.code == 403) {
            sessionStorage.clear()
            AuthSessionEventBus.notifySessionExpired()
        }

        return response
    }
}