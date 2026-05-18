package com.jfb.orderops.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R

@Composable
fun DashboardHeader(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    append("Coman")
                    withStyle(
                        style = SpanStyle(
                            color = colors.primary
                        )
                    ) {
                        append("dex")
                    }
                },
                style = MaterialTheme.typography.headlineLarge,
                color = colors.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Painel operacional",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }

        TextButton(
            onClick = onLogout
        ) {
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Sair ",
                color = colors.primary,
                fontWeight = FontWeight.SemiBold
            )

            Icon(
                painter = painterResource(R.drawable.ic_log_out),
                contentDescription = "Sair",
                tint = colors.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}