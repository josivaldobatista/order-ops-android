package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.serviceTable.data.repository.ServiceTableRepositoryImpl
import com.jfb.orderops.serviceTable.domain.usecase.CreateServiceTableUseCase
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableScreen
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableViewModel
import com.jfb.orderops.serviceTable.presentation.create.CreateServiceTableViewModelFactory

fun NavGraphBuilder.serviceTableGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    composable(AppRoute.CreateServiceTable.route) {
        val serviceTableApi =
            RetrofitClient.createServiceTableApi(sessionStorage)

        val repository =
            ServiceTableRepositoryImpl(serviceTableApi)

        val useCase =
            CreateServiceTableUseCase(repository)

        val viewModel: CreateServiceTableViewModel = viewModel(
            factory = CreateServiceTableViewModelFactory(useCase)
        )

        val uiState =
            viewModel.uiState.collectAsState().value

        CreateServiceTableScreen(
            uiState = uiState,
            onNumberChange = viewModel::onNumberChange,
            onCapacityChange = viewModel::onCapacityChange,
            onCreate = {
                viewModel.create {
                    navController.popBackStack()
                }
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
}