package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComandexBottomBar(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, label ->
            val selected = selectedIndex == index

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onItemSelected(index) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (label) {
                        "Home" -> "⌂"
                        "Mesas" -> "▱"
                        "Pedidos" -> "☰"
                        "Produtos" -> "▣"
                        "Relatórios" -> "▥"
                        else -> "•"
                    },
                    color = if (selected) colors.primary else colors.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = label,
                    color = if (selected) colors.primary else colors.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}