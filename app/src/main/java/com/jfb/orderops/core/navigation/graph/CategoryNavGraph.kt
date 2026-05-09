package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.category.data.repository.CategoryRepositoryImpl
import com.jfb.orderops.category.domain.usecase.CreateCategoryUseCase
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.category.domain.usecase.UpdateCategoryUseCase
import com.jfb.orderops.category.presentation.create.CreateCategoryScreen
import com.jfb.orderops.category.presentation.create.CreateCategoryViewModel
import com.jfb.orderops.category.presentation.create.CreateCategoryViewModelFactory
import com.jfb.orderops.category.presentation.list.CategoriesScreen
import com.jfb.orderops.category.presentation.list.CategoriesViewModel
import com.jfb.orderops.category.presentation.list.CategoriesViewModelFactory
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage

fun NavGraphBuilder.categoryGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {
    composable(AppRoute.CreateCategory.route) {
        val categoryApi = RetrofitClient.createCategoryApi(sessionStorage)
        val categoryRepository = CategoryRepositoryImpl(categoryApi)
        val createCategoryUseCase = CreateCategoryUseCase(categoryRepository)

        val viewModel: CreateCategoryViewModel = viewModel(
            factory = CreateCategoryViewModelFactory(
                createCategoryUseCase
            )
        )

        val uiState = viewModel.uiState.collectAsState().value

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

    composable(AppRoute.Categories.route) {
        val categoryApi = RetrofitClient.createCategoryApi(sessionStorage)
        val categoryRepository = CategoryRepositoryImpl(categoryApi)
        val listCategoriesUseCase = ListCategoriesUseCase(categoryRepository)
        val updateCategoryUseCase = UpdateCategoryUseCase(categoryRepository)

        val viewModel: CategoriesViewModel = viewModel(
            factory = CategoriesViewModelFactory(
                listCategoriesUseCase = listCategoriesUseCase,
                updateCategoryUseCase = updateCategoryUseCase
            )
        )

        val uiState = viewModel.uiState.collectAsState().value

        CategoriesScreen(
            uiState = uiState,
            onRefresh = viewModel::loadCategories,
            onCreateCategory = {
                navController.navigate(AppRoute.CreateCategory.route)
            },
            onCategoryClick = viewModel::openEditDialog,
            onEditNameChange = viewModel::onEditNameChange,
            onUpdateCategory = viewModel::updateCategory,
            onDismissDialog = viewModel::closeEditDialog,
            onBack = {
                navController.popBackStack()
            }
        )
    }
}