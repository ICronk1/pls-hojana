package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RomanticWhiteAndPinkColorScheme = lightColorScheme(
    primary = RomanticPink,
    onPrimary = CrispWhite,
    primaryContainer = BlushPink,
    onPrimaryContainer = TextPrimary,
    secondary = SoftPink,
    onSecondary = CrispWhite,
    secondaryContainer = SurfacePinkLight,
    onSecondaryContainer = TextPrimary,
    tertiary = DeepRubyRed,
    onTertiary = CrispWhite,
    background = WhiteBackground,
    onBackground = TextPrimary,
    surface = CrispWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfacePinkLight,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceCardBorder,
    outlineVariant = BorderSubtle
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RomanticWhiteAndPinkColorScheme,
        typography = Typography,
        content = content
    )
}
