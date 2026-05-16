package com.jfb.orderops.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jfb.orderops.R
import com.jfb.orderops.auth.presentation.state.LoginUiState
import com.jfb.orderops.ui.theme.LocalOrderOpsExtraColors
import com.jfb.orderops.ui.theme.OrderOpsTheme

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val extraColors = LocalOrderOpsExtraColors.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            extraColors.backgroundTop,
                            colors.background
                        )
                    )
                )
        ) {
            LoginDecorations(colors)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 32.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandHeader(colors)

                Spacer(Modifier.height(34.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Bem-vindo de volta!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )

                    Text(
                        text = "Faça login para continuar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(20.dp))

                LoginTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = "E-mail",
                    colors = colors,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.height(14.dp))

                LoginTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = "Senha",
                    colors = colors,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    isPassword = true
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(colors.primary.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Lembrar-me",
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = "Esqueci minha senha",
                        color = colors.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                uiState.errorMessage?.let {
                    Spacer(Modifier.height(14.dp))
                    FeedbackMessage(
                        text = it,
                        color = colors.error
                    )
                }

                if (uiState.isLoggedIn) {
                    Spacer(Modifier.height(14.dp))
                    FeedbackMessage(
                        text = "Login realizado com sucesso!",
                        color = extraColors.success
                    )
                }

                Spacer(Modifier.height(20.dp))

                GradientLoginButton(
                    text = "Entrar",
                    isLoading = uiState.isLoading,
                    onClick = onLoginClick,
                    colors = colors
                )

                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.outline.copy(alpha = 0.45f)
                    )

                    Text(
                        text = "  ou  ",
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.outline.copy(alpha = 0.45f)
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Precisa de ajuda? Fale com o suporte",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BrandHeader(
    colors: ColorScheme
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.comandex_logo),
            contentDescription = "Logo Comandex",
            modifier = Modifier.size(width = 150.dp, height = 88.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Coman",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = "dex",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Gestão inteligente para restaurantes",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    colors: ColorScheme,
    leadingIcon: @Composable () -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.onBackground,
            unfocusedTextColor = colors.onBackground,
            focusedLabelColor = colors.primary,
            unfocusedLabelColor = colors.onSurfaceVariant,
            focusedLeadingIconColor = colors.primary,
            unfocusedLeadingIconColor = colors.onSurfaceVariant,
            cursorColor = colors.primary,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline,
            focusedContainerColor = colors.surface.copy(alpha = 0.42f),
            unfocusedContainerColor = colors.surface.copy(alpha = 0.28f)
        )
    )
}

@Composable
private fun GradientLoginButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    colors: ColorScheme
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = colors.primary
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        colors.primary,
                        Color(0xFFF4A261)
                    )
                )
            )
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LoginDecorations(
    colors: ColorScheme
) {
    Box(
        modifier = Modifier
            .offset(x = (-90).dp, y = (-120).dp)
            .size(230.dp)
            .background(
                color = colors.primary.copy(alpha = 0.08f),
                shape = CircleShape
            )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 84.dp, end = 32.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(5) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .background(
                                color = colors.primary.copy(alpha = 0.45f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackMessage(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    OrderOpsTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "admin@jottasburguer.com",
                password = "123456"
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}