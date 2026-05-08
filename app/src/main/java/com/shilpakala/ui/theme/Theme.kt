package com.shilpakala.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ── Shilpa-Kala Light Color Scheme ───────────────────
private val ShilpaKalaColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = WhiteClean,
    primaryContainer = TerracottaLight,
    onPrimaryContainer = WhiteClean,

    secondary = HeritagGold,
    onSecondary = DeepBrown,
    secondaryContainer = HeritageGoldLight,
    onSecondaryContainer = DeepBrown,

    background = WarmCream,
    onBackground = DeepBrown,

    surface = SoftParchment,
    onSurface = DeepBrown,

    surfaceVariant = LightSand,
    onSurfaceVariant = MediumBrown,

    error = ErrorRed,
    onError = WhiteClean,
)

@Composable
fun ShilpaKalaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ShilpaKalaColorScheme,
        typography = Typography,
        content = content
    )
}