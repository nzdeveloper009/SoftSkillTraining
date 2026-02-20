package com.cmp.community.healers.softskilltraining.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ─── Color Scheme ─────────────────────────────────────────────────────────────
val LightColors = lightColorScheme(
    primary          = Green800,
    onPrimary        = NeutralWhite,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary        = Green600,
    background       = NeutralWhite,
    surface          = NeutralWhite,
    onBackground     = NeutralGray900,
    onSurface        = NeutralGray900,
    outline          = NeutralGray400
)



//val DarkColors = darkColorScheme(
//    primary          = Green800,
//    onPrimary        = NeutralWhite,
//    primaryContainer = Green100,
//    onPrimaryContainer = Green900,
//    secondary        = Green600,
//    background       = NeutralWhite,
//    surface          = NeutralWhite,
//    onBackground     = NeutralGray900,
//    onSurface        = NeutralGray900,
//    outline          = NeutralGray400
//)


val LightColorTheme = lightColorScheme(
    primary = Primary,
    surface = Surface,
    surfaceContainerLowest = SurfaceLowest,
    background = Background,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant
)

@Composable
fun extendedColor(light: Color, dark: Color): Color {
    return if(isSystemInDarkTheme()) dark else light
}

val ColorScheme.extraColor: Color @Composable get() = extendedColor(
    light = Color(0xFF000000),
    dark = Color(0xFFFFFFFF)
)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(15.dp)
)


// ─── Theme ────────────────────────────────────────────────────────────────────
@Composable
fun CommunityHealerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        shapes = Shapes,
        content     = content
    )
}