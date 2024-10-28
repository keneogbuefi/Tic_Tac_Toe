package com.example.TicTacToe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF030610),
    secondary =  Color(0xFF030610),
    background = Color(0xFF030610),
    tertiary = Color(0xFF030610),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF21557A),
    secondary = Color(0xFF225477),
    tertiary = Color(0xFFF5F5F5),
    background = Color(0xFFEFE1E1),
    surface = Color(0xFF0D466E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),

)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}