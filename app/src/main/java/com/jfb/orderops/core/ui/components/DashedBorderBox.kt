package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedBorderBox(
    modifier: Modifier = Modifier,
    color: Color,
    cornerRadius: Dp = 14.dp,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 5.dp,
    gapWidth: Dp = 5.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                val strokePx = strokeWidth.toPx()

                drawRoundRect(
                    color = color,
                    size = Size(
                        width = size.width,
                        height = size.height
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        cornerRadius.toPx(),
                        cornerRadius.toPx()
                    ),
                    style = Stroke(
                        width = strokePx,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                dashWidth.toPx(),
                                gapWidth.toPx()
                            )
                        )
                    )
                )
            }
            .padding(contentPadding)
    ) {
        content()
    }
}