package com.jfb.orderops.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MyLightColorScheme = lightColorScheme(
    primary = Primary,
    background = Background,
    surface = Surface,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8FAADC),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8FAADC),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun OrderOpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}