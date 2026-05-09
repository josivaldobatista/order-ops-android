package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.auth.data.repository.AuthRepositoryImpl
import com.jfb.orderops.auth.domain.usecase.LoginUseCase
import com.jfb.orderops.auth.presentation.login.LoginScreen
import com.jfb.orderops.auth.presentation.login.LoginViewModel
import com.jfb.orderops.auth.presentation.login.LoginViewModelFactory
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    composable(AppRoute.Login.route) {
        val authApi =
            RetrofitClient.createAuthApi(sessionStorage)

        val authRepository =
            AuthRepositoryImpl(
                api = authApi,
                sessionStorage = sessionStorage
            )

        val loginUseCase =
            LoginUseCase(authRepository)

        val viewModel: LoginViewModel = viewModel(
            factory = LoginViewModelFactory(loginUseCase)
        )

        val uiState =
            viewModel.uiState.collectAsState().value

        LoginScreen(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login
        )

        LaunchedEffect(uiState.isLoggedIn) {
            if (uiState.isLoggedIn) {
                navController.navigate(AppRoute.Dashboard.route) {
                    popUpTo(AppRoute.Login.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }
}