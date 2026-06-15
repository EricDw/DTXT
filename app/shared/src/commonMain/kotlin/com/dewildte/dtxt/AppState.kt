package com.dewildte.dtxt

import androidx.compose.runtime.*
import com.dewildte.dtxt.content.ContentType
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFile

@Stable
class AppState(
    fileStatus: SelectedFile.Status = SelectedFile.Status.LOADING,
    selectedFile: TextFile = TextFile(),
    error: Throwable? = null,
    contentType: ContentType = ContentType.EDITOR,
) {

    var contentType: ContentType by mutableStateOf(contentType)

    var error: Throwable? by mutableStateOf(error)

    var fileStatus: SelectedFile.Status by mutableStateOf(fileStatus)

    var selectedFile: TextFile by mutableStateOf(selectedFile)
}

@Composable
fun rememberAppState(
    fileStatus: SelectedFile.Status = SelectedFile.Status.LOADING,
    selectedFile: TextFile = TextFile(),
    error: Throwable? = null,
    contentType: ContentType = ContentType.EDITOR,
): AppState {
    return remember {
        AppState(
            fileStatus = fileStatus,
            selectedFile = selectedFile,
            error = error,
            contentType = contentType,
        )
    }
}
