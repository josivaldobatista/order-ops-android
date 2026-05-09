package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.category.data.repository.CategoryRepositoryImpl
import com.jfb.orderops.category.domain.usecase.CreateCategoryUseCase
import com.jfb.orderops.category.presentation.create.CreateCategoryScreen
import com.jfb.orderops.category.presentation.create.CreateCategoryViewModel
import com.jfb.orderops.category.presentation.create.CreateCategoryViewModelFactory
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage

fun NavGraphBuilder.categoryGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {

    composable(AppRoute.CreateCategory.route) {

        val categoryApi =
            RetrofitClient.createCategoryApi(sessionStorage)

        val categoryRepository =
            CategoryRepositoryImpl(categoryApi)

        val createCategoryUseCase =
            CreateCategoryUseCase(categoryRepository)

        val viewModel: CreateCategoryViewModel = viewModel(
            factory = CreateCategoryViewModelFactory(
                createCategoryUseCase
            )
        )

        val uiState =
            viewModel.uiState.collectAsState().value

        CreateCategoryScreen(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
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