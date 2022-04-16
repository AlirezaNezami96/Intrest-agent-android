package com.mindlab.intrest_agent_android.presentation.components.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val LightThemeColors = lightColors(
    primary = GREEN,
    onPrimary = WHITE,
    secondary = GRAY_100,
    secondaryVariant = GRAY_900,
    onSecondary = Black_3,
    error = RedError,
    background = WHITE,
    onBackground = Black_3,
    surface = Color.White,
    onSurface = Black2,
    onError = WHITE
)

private val DarkThemeColors = darkColors(
    primary = GREEN,
    onPrimary = WHITE,
    primaryVariant = GRAY_700,
    secondary = GRAY_200,
    secondaryVariant = GRAY_400,
    onSecondary = Black_3,
    error = RedError,
    background = WHITE,
    onBackground = Black_3,
    surface = Color.White,
    onSurface = Black2,
)

@Composable
fun IntRestagentandroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}