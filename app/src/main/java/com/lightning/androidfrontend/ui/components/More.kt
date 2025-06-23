package com.lightning.androidfrontend.ui.components

import androidx.compose.runtime.Composable
import java.io.Serializable

interface More: Serializable {
    var icon: Int
    var name: String

    fun setObjectToSend(): com.lightning.androidfrontend.ui.components.More

    @Composable
    fun setContent()

}