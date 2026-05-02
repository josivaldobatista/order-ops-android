package com.jfb.orderops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.jfb.orderops.core.navigation.AppNavHost
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.ui.theme.OrderOpsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionStorage = SessionStorage(applicationContext)

        setContent {
            OrderOpsTheme {
                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    sessionStorage = sessionStorage
                )
            }
        }
    }
}