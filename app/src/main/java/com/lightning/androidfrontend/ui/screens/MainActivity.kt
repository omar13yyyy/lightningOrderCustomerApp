package com.lightning.androidfrontend.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lightning.androidfrontend.navigation.MyNavHost
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.utils.LanguagePreferences
import com.lightning.androidfrontend.utils.LanguagePreferences.updateLocale
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val locale = Locale("ar")
        val updatedContext = newBase.updateLocale(locale)
        LanguagePreferences.save(updatedContext,"ar")
        super.attachBaseContext(updatedContext)
    }
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAppTheme {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(color = Color.White, darkIcons = true)
             Locale("ar")
            val navController = rememberAnimatedNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNavHost(navHostController = navController)
                }
            }
        }
    }
}