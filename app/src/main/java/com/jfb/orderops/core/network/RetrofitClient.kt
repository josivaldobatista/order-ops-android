package com.jfb.orderops.core.network

import com.jfb.orderops.auth.data.remote.AuthApi
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.order.data.remote.OrderApi
import com.jfb.orderops.serviceTable.data.remote.ServiceTableApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jfb.orderops.product.data.remote.ProductApi

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.14:8080/"

    fun createAuthApi(sessionStorage: SessionStorage): AuthApi {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(sessionStorage)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApi::class.java)
    }

    fun createServiceTableApi(sessionStorage: SessionStorage): ServiceTableApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(sessionStorage)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ServiceTableApi::class.java)
    }

    fun createOrderApi(sessionStorage: SessionStorage): OrderApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(sessionStorage)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OrderApi::class.java)
    }

    fun createProductApi(sessionStorage: SessionStorage): ProductApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val auth = AuthInterceptor(sessionStorage)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(auth)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ProductApi::class.java)
    }
}