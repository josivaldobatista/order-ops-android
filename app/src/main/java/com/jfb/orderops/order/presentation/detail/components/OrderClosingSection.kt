package com.jfb.orderops.order.presentation.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderClosingSection(
    content: @Composable () -> Unit
) {

    Column {

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        content()
    }
}