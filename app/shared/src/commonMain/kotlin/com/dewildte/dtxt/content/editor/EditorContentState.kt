package com.dewildte.dtxt.content.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dewildte.dtxt.data.TextFile

@Stable
class EditorContentState(
    textFile: TextFile = TextFile(),
    searchMode: Boolean = false,
    searchTerm: String = "",
    contextMenuExpanded: Boolean = false,
) {
    var textFile: TextFile by mutableStateOf(textFile)

    var searchMode: Boolean by mutableStateOf(searchMode)

    var searchTerm: String by mutableStateOf(searchTerm)

    var moreMenuExpanded: Boolean by mutableStateOf(contextMenuExpanded)
}

@Composable
fun rememberEditorContentState(
    textFile: TextFile = TextFile(),
): EditorContentState {
    return remember {
        EditorContentState(
            textFile = textFile,
        )
    }
}
