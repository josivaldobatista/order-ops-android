package com.jfb.orderops.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jfb.orderops.ui.theme.OrderOpsTheme

@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dashboard")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    OrderOpsTheme {
        DashboardScreen(
            onLogout = {}
        )
    }
}