package com.jfb.orderops.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jfb.orderops.auth.data.repository.AuthRepositoryImpl
import com.jfb.orderops.auth.domain.usecase.LoginUseCase
import com.jfb.orderops.auth.presentation.login.LoginScreen
import com.jfb.orderops.auth.presentation.login.LoginViewModel
import com.jfb.orderops.auth.presentation.login.LoginViewModelFactory
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.dashboard.presentation.DashboardScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    val authApi = RetrofitClient.createAuthApi(sessionStorage)

    val repository = AuthRepositoryImpl(
        api = authApi,
        sessionStorage = sessionStorage
    )

    val useCase = LoginUseCase(repository)

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(useCase)
    )

    val uiState = loginViewModel.uiState.collectAsState().value

    val startDestination = if (sessionStorage.isLoggedIn()) {
        AppRoute.Dashboard.route
    } else {
        AppRoute.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                uiState = uiState,
                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onLoginClick = {
                    loginViewModel.login()
                }
            )

            if (uiState.isLoggedIn) {
                navController.navigate(AppRoute.Dashboard.route) {
                    popUpTo(AppRoute.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(AppRoute.Dashboard.route) {
            DashboardScreen(
                sessionStorage = sessionStorage,
                onLogout = {
                    sessionStorage.clear()

                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.Dashboard.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}