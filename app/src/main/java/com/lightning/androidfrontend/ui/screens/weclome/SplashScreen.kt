package com.lightning.androidfrontend.ui.screens.weclome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.lightning.androidfrontend.R
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.ui.screens.Logo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigateToPageView: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(500)
        navigateToPageView()
    }
    MyAppTheme {
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_splash_background),
                contentDescription = null,
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Logo(modifier = Modifier.align(Alignment.Center))
        }
    }
}