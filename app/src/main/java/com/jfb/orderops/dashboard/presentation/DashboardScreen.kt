package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jfb.orderops.core.network.RetrofitClient
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.serviceTable.data.repository.ServiceTableRepositoryImpl
import com.jfb.orderops.serviceTable.domain.usecase.ListServiceTablesUseCase
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesScreen
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesViewModel
import com.jfb.orderops.serviceTable.presentation.list.ServiceTablesViewModelFactory

@Composable
fun DashboardScreen(
    sessionStorage: SessionStorage,
    onLogout: () -> Unit
) {
    val serviceTableApi = RetrofitClient.createServiceTableApi(sessionStorage)

    val serviceTableRepository = ServiceTableRepositoryImpl(
        api = serviceTableApi
    )

    val listServiceTablesUseCase = ListServiceTablesUseCase(
        repository = serviceTableRepository
    )

    val serviceTablesViewModel: ServiceTablesViewModel = viewModel(
        factory = ServiceTablesViewModelFactory(listServiceTablesUseCase)
    )

    val uiState = serviceTablesViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        serviceTablesViewModel.loadServiceTables()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "OrderOps",
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = onLogout) {
                Text("Logout")
            }
        }

        ServiceTablesScreen(
            uiState = uiState,
            onRefresh = serviceTablesViewModel::loadServiceTables
        )
    }
}