package com.lightning.androidfrontend.ui.screens.home

import androidx.compose.runtime.Composable
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.ui.screens.AppTopBar
import com.lightning.androidfrontend.theme.MyAppTheme

@Composable
fun ChangeAddressScreen(){
    MyAppTheme() {
        AppTopBar(title = "Change Address", backIcon = true)
    }
}