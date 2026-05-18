package com.jfb.orderops.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalOrderOpsExtraColors provides OrderOpsExtraDarkColors
    ) {
        MaterialTheme(
            colorScheme = OrderOpsDarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}