package com.dewildte.dtxt.data

import androidx.compose.runtime.Immutable

@Immutable
data class TextFile(
    val path: String = "",
    val contents: String = "",
) {
    companion object {
        val EMPTY = TextFile()
    }
}