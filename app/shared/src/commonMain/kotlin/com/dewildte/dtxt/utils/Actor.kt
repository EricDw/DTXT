package com.dewildte.dtxt.utils

import androidx.compose.runtime.Stable

@Stable
fun interface Actor {
    fun tell(message: Any)
}