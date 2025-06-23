package com.lightning.androidfrontend.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.staticCompositionLocalOf
data class Spacing(
    val none: Dp = 0.dp,
    val tiny: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp
)

//in component val spacing = LocalSpacing.current

val LocalSpacing = staticCompositionLocalOf { Spacing() }