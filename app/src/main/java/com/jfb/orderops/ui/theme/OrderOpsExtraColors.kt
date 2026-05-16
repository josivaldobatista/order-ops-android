package com.jfb.orderops.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class OrderOpsExtraColors(
    val backgroundTop: Color,
    val success: Color,
    val warning: Color
)

val LocalOrderOpsExtraColors = staticCompositionLocalOf {
    OrderOpsExtraColors(
        backgroundTop = Color.Unspecified,
        success = Color.Unspecified,
        warning = Color.Unspecified
    )
}