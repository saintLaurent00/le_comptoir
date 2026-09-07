package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ComptoirColorScheme = lightColorScheme(
    primary = ComptoirBgDark,
    onPrimary = ComptoirBgPrimary,
    secondary = ComptoirAccentGold,
    onSecondary = ComptoirBgPrimary,
    tertiary = ComptoirAccentTeal,
    onTertiary = ComptoirBgCard,
    background = ComptoirBgPrimary,
    onBackground = ComptoirTextPrimary,
    surface = ComptoirBgCard,
    onSurface = ComptoirTextPrimary,
    surfaceVariant = ComptoirBgSurface,
    onSurfaceVariant = ComptoirTextSecondary,
    outline = ComptoirBorderLight,
    error = ComptoirAccentRed,
    onError = ComptoirBgCard
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ComptoirColorScheme,
        typography = Typography,
        content = content
    )
}
