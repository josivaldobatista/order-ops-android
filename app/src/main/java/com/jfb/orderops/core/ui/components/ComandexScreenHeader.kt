package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors

@Composable
fun ComandexScreenHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    enabled: Boolean = true,
    trailing: @Composable (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            enabled = enabled,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface.copy(alpha = 0.48f))
                .border(
                    width = 1.dp,
                    color = colors.outline.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(14.dp)
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = "Voltar",
                tint = colors.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(extraColors.success)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        trailing?.invoke()
    }
}

@Composable
fun ComandexStatusBadge(
    text: String,
    type: ComandexBadgeType
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    val color = when (type) {
        ComandexBadgeType.SUCCESS -> extraColors.success
        ComandexBadgeType.PRIMARY -> colors.primary
        ComandexBadgeType.WARNING -> extraColors.warning
        ComandexBadgeType.ERROR -> colors.error
        ComandexBadgeType.NEUTRAL -> colors.outline
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = color.copy(alpha = 0.18f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

enum class ComandexBadgeType {
    SUCCESS,
    PRIMARY,
    WARNING,
    ERROR,
    NEUTRAL
}