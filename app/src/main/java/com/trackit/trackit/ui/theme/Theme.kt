package com.trackit.trackit.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue700,
    secondary = PurpleInterview,
    tertiary = GreenSuccess,
    background = Gray100,
    surface = androidx.compose.ui.graphics.Color.White,
    onBackground = Gray900,
    onSurface = Gray900,
    error = RedError,
    outline = Gray500
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = Gray900,
    primaryContainer = Blue700,
    onPrimaryContainer = Blue100,
    secondary = PurpleInterview,
    tertiary = GreenSuccess,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    error = RedError,
    outline = Gray500
)

@Composable
fun TrackItTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}