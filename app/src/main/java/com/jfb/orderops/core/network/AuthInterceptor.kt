package com.jfb.orderops.core.network

import com.jfb.orderops.core.storage.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionStorage: SessionStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = sessionStorage.getAccessToken()
        val tokenType = sessionStorage.getTokenType()

        val requestBuilder = originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader(
                "Authorization",
                "$tokenType $token"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}