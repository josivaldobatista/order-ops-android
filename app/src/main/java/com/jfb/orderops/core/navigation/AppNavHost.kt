package com.jfb.orderops.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.jfb.orderops.core.auth.AuthSessionEvent
import com.jfb.orderops.core.auth.AuthSessionEventBus
import com.jfb.orderops.core.navigation.graph.authGraph
import com.jfb.orderops.core.navigation.graph.categoryGraph
import com.jfb.orderops.core.navigation.graph.dashboardGraph
import com.jfb.orderops.core.navigation.graph.orderGraph
import com.jfb.orderops.core.navigation.graph.paymentGraph
import com.jfb.orderops.core.navigation.graph.productGraph
import com.jfb.orderops.core.navigation.graph.receiptGraph
import com.jfb.orderops.core.navigation.graph.serviceTableGraph
import com.jfb.orderops.core.storage.SessionStorage

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {

    val startDestination = if (sessionStorage.isLoggedIn()) {
        AppRoute.Dashboard.route
    } else {
        AppRoute.Login.route
    }

    LaunchedEffect(Unit) {
        AuthSessionEventBus.events.collect { event ->
            when (event) {
                AuthSessionEvent.SessionExpired -> {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        authGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        dashboardGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        productGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        categoryGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        orderGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        paymentGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        receiptGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )

        serviceTableGraph(
            navController = navController,
            sessionStorage = sessionStorage
        )
    }
}