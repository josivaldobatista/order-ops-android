package com.jfb.orderops.core.navigation

sealed class AppRoute(
    val route: String
) {
    data object Login : AppRoute("login")
    data object Dashboard : AppRoute("dashboard")
}