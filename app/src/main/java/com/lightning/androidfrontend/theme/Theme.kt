package com.lightning.androidfrontend.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.*

@Composable
fun MyAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    //val appColors = if (useDarkTheme) DarkAppColors else LightAppColors
    val appColors = LightAppColors
    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(), // اختيارية
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

