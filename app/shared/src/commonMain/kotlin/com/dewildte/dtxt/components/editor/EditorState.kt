package com.dewildte.dtxt.components.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember

@Immutable
data class EditorState(
    val initialText: String,
)

@Composable
fun rememberEditorState(
    initialText: String = "",
): EditorState {
    return remember {
        EditorState(initialText)
    }
}
