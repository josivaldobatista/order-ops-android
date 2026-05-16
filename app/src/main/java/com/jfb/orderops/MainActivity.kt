package com.jfb.orderops

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.jfb.orderops.core.navigation.AppNavHost
import com.jfb.orderops.core.storage.SessionStorage
import com.jfb.orderops.ui.theme.OrderOpsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionStorage = SessionStorage(applicationContext)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

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