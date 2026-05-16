package com.jfb.orderops.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val OrderOpsDarkColorScheme = darkColorScheme(
    primary = OrderOpsPrimary,
    onPrimary = OrderOpsTextPrimary,

    secondary = OrderOpsPrimaryVariant,
    onSecondary = OrderOpsBackground,

    background = OrderOpsBackground,
    onBackground = OrderOpsTextPrimary,

    surface = OrderOpsSurface,
    onSurface = OrderOpsTextPrimary,

    surfaceVariant = OrderOpsSurfaceElevated,
    onSurfaceVariant = OrderOpsTextSecondary,

    error = OrderOpsError,
    onError = OrderOpsTextPrimary,

    outline = OrderOpsOutline
)

private val OrderOpsExtraDarkColors = OrderOpsExtraColors(
    backgroundTop = OrderOpsBackgroundTop,
    success = OrderOpsSuccess,
    warning = OrderOpsWarning
)

@Composable
fun OrderOpsTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> OrderOpsDarkColorScheme
    }

    CompositionLocalProvider(
        LocalOrderOpsExtraColors provides OrderOpsExtraDarkColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}