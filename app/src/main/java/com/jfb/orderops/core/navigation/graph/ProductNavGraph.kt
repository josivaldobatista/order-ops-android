package com.jfb.orderops.core.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jfb.orderops.category.data.repository.CategoryRepositoryImpl
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.navigation.AppRoute
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.product.data.repository.ProductRepositoryImpl
import com.jfb.orderops.product.domain.usecase.CreateProductUseCase
import com.jfb.orderops.product.presentation.create.CreateProductScreen
import com.jfb.orderops.product.presentation.create.CreateProductViewModel
import com.jfb.orderops.product.presentation.create.CreateProductViewModelFactory

fun NavGraphBuilder.productGraph(
    navController: NavHostController,
    sessionStorage: SessionStorage
) {

    composable(AppRoute.CreateProduct.route) {

        val productApi =
            RetrofitClient.createProductApi(sessionStorage)

        val productRepository =
            ProductRepositoryImpl(productApi)

        val createProductUseCase =
            CreateProductUseCase(productRepository)

        val categoryApi =
            RetrofitClient.createCategoryApi(sessionStorage)

        val categoryRepository =
            CategoryRepositoryImpl(categoryApi)

        val listCategoriesUseCase =
            ListCategoriesUseCase(categoryRepository)

        val viewModel: CreateProductViewModel = viewModel(
            factory = CreateProductViewModelFactory(
                createProductUseCase = createProductUseCase,
                listCategoriesUseCase = listCategoriesUseCase
            )
        )

        val uiState =
            viewModel.uiState.collectAsState().value

        CreateProductScreen(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onPriceChange = viewModel::onPriceChange,
            onCategorySelected = viewModel::onCategorySelected,
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