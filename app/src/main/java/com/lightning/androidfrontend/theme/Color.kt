package com.lightning.androidfrontend.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf


val orange = Color(0xFFFC6011)
val primaryFontColor = Color(0xFF4A4B4D)
val secondaryFontColor = Color(0xFF7C7D7E)
val placeholderColor = Color(0xFFB6B7B7)
val gray = Color(0xFFF2F2F2)
val gray2 = Color(0xFFF6F6F6)
val gray3 = Color(0xFFD8D8D8)
val gray4 = Color(0xFFEEEEEE)
val blue = Color(0xFF367FC0)
val red = Color(0xFFDD4B39)
val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)



data class AppColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val orange: Color,
    val error: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val  chooseLocationColor: Color,
    val  chooseLocationFieldColor: Color,
    val  chooseLocationButtonColor: Color,
    val  productDetailsColor: Color,
    val productDetailsColorLighter:Color,

    val gray40: Color,
    val gray80: Color,
    val  Yellow80: Color,
    val  Yellow60: Color,
    val  GreyScale40: Color,
    val  GreenGrey40: Color,

    val onBackground: Color,
    val onSurface: Color,
    val onError: Color,
    val isLight: Boolean
)
val LightAppColors = AppColors(
    primary = Color(0xFF0A8754),
    primaryVariant = Color(0xFF088F6F),
    secondary = Color(0xFFF4C95D),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    orange=Color(0xFFFC6011),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    chooseLocationColor =Color(0xFFFFEEAD),
    chooseLocationButtonColor = Color(0xFFFFAD60),
    chooseLocationFieldColor = Color(0xFF96CEB4),
    productDetailsColor = Color(0xFFFC6011),
    productDetailsColorLighter = Color(0xFFFF7a58),


    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    gray80     = Color(0xFFCCCCCC), // 80% من الأبيض (gray)
    gray40     = Color(0xFF666666), // 40% من الأبيض (gray)
    Yellow80   = Color(0xFFFFF176), // 80% من الأصفر (قريب من Light Yellow)
    Yellow60   = Color(0xFFFFEE58), // 60% من الأصفر (أفتح قليلًا)
    GreyScale40= Color(0xFF999999),
    GreenGrey40 = Color(0xFF78917C), // درجة رمادية في منتصف بين gray40 وgray80
    isLight = true
)

val DarkAppColors = AppColors(
    primary = Color(0xFF53D7A5),
    primaryVariant = Color(0xFF1EB980),
    secondary = Color(0xFFFFE082),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    orange=Color(0xFFFC6011),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    chooseLocationColor =Color.LightGray,
    chooseLocationButtonColor = Color(0xFFFFAD60),
    chooseLocationFieldColor = Color(0xFF96CEB4),
    productDetailsColor = Color(0xFFFC6011),
    productDetailsColorLighter = Color(0xFFFF7a58),

    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    gray80     = Color(0xFFCCCCCC), // 80% من الأبيض (gray)
    gray40     = Color(0xFF666666), // 40% من الأبيض (gray)
    Yellow80   = Color(0xFFFFF176), // 80% من الأصفر (قريب من Light Yellow)
    Yellow60   = Color(0xFFFFEE58), // 60% من الأصفر (أفتح قليلًا)
    GreyScale40= Color(0xFF999999),
    GreenGrey40 = Color(0xFF78917C),// درجة رمادية في منتصف بين gray40 وgray80
    isLight = false
)


val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}

//usage     val colors = LocalAppColors.current