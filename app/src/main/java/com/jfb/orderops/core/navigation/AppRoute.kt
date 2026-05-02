package com.jfb.orderops.core.navigation

sealed class AppRoute(
    val route: String
) {
    data object Login : AppRoute("login")

    data object Dashboard : AppRoute("dashboard")

    data object CreateOrder : AppRoute("create-order/{serviceTableId}") {
        fun createRoute(serviceTableId: Long): String {
            return "create-order/$serviceTableId"
        }
    }

    data object OrderDetail : AppRoute("order-detail/{orderId}") {
        fun createRoute(orderId: Long): String {
            return "order-detail/$orderId"
        }
    }
}