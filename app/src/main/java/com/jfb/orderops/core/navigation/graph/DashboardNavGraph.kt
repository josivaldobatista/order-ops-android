package com.jfb.orderops.core.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.dashboard.presentation.DashboardScreen

fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {

    composable(AppRoute.Dashboard.route) {

        DashboardScreen(
            sessionStorage = sessionStorage,
            navController = navController,
            onLogout = {

                sessionStorage.clear()

                navController.navigate(AppRoute.Login.route) {

                    popUpTo(AppRoute.Dashboard.route) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }
        )
    }
}