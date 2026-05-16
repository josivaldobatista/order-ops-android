package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors

@Composable
fun ComandexScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        extraColors.backgroundTop,
                        colors.background
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        content()
    }
}