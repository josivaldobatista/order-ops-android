package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComandexTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = selectedIndex == index

            Text(
                text = title,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        color = if (selected) {
                            colors.primary
                        } else {
                            colors.surfaceVariant
                        }
                    )
                    .clickable { onSelected(index) }
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                color = if (selected) {
                    colors.onPrimary
                } else {
                    colors.onSurfaceVariant
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}