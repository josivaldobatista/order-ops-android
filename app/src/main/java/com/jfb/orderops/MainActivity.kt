package com.jfb.orderops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jfb.orderops.auth.data.repository.AuthRepositoryImpl
import com.jfb.orderops.auth.domain.usecase.LoginUseCase
import com.jfb.orderops.auth.presentation.login.LoginScreen
import com.jfb.orderops.auth.presentation.login.LoginViewModel
import com.jfb.orderops.auth.presentation.login.LoginViewModelFactory
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.dashboard.presentation.DashboardScreen
import com.jfb.orderops.ui.theme.OrderOpsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionStorage = SessionStorage(applicationContext)

        setContent {
            OrderOpsTheme {
                val authApi = RetrofitClient.createAuthApi(sessionStorage)

                val repository = AuthRepositoryImpl(
                    api = authApi,
                    sessionStorage = sessionStorage
                )

                val useCase = LoginUseCase(repository)

                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(useCase)
                )

                val uiState = viewModel.uiState.collectAsState().value

                val isLoggedIn = sessionStorage.isLoggedIn() || uiState.isLoggedIn

                if (isLoggedIn) {
                    DashboardScreen(
                        sessionStorage = sessionStorage,
                        onLogout = {
                            sessionStorage.clear()
                        }
                    )
                } else {
                    LoginScreen(
                        uiState = uiState,
                        onEmailChange = viewModel::onEmailChange,
                        onPasswordChange = viewModel::onPasswordChange,
                        onLoginClick = viewModel::login
                    )
                }
            }
        }
    }
}