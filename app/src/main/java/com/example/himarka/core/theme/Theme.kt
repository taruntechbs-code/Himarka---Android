package com.example.himarka.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val HimarkaLightColorScheme = lightColorScheme(
    primary = HimarkaViolet,
    onPrimary = HimarkaCardBackground,
    primaryContainer = HimarkaViolet.copy(alpha = 0.12f),
    onPrimaryContainer = HimarkaViolet,
    secondary = HimarkaSky,
    onSecondary = HimarkaCardBackground,
    tertiary = HimarkaEmerald,
    background = HimarkaCanvas,
    onBackground = HimarkaTextMain,
    surface = HimarkaCardBackground,
    onSurface = HimarkaTextMain,
    surfaceVariant = HimarkaCanvas,
    onSurfaceVariant = HimarkaTextMuted,
    outline = HimarkaCardBorder
)

@Composable
fun HimarkaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HimarkaLightColorScheme,
        typography = Typography,
        shapes = HimarkaShapes,
        content = content
    )
}
