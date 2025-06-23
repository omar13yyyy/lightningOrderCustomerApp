package com.lightning.androidfrontend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostVirtualRes(
    val message: String,
    val success: Boolean
)
