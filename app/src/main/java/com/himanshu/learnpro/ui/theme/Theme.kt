package com.himanshu.learnpro.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SoftBlack,
    background = LightBackground,
    surface = SurfaceWhite,
    onPrimary = SurfaceWhite,
    onBackground = SoftBlack,
    onSurface = SoftBlack
)

@Composable
fun LearnProTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
