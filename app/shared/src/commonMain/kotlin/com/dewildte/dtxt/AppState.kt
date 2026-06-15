package com.dewildte.dtxt

import androidx.compose.runtime.*
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFile

@Stable
class AppState(
    fileStatus: SelectedFile.Status = SelectedFile.Status.LOADING,
    selectedFile: TextFile = TextFile(),
    error: Throwable? = null,
) {
    var error: Throwable? by mutableStateOf(error)

    var fileStatus: SelectedFile.Status by mutableStateOf(fileStatus)

    var selectedFile: TextFile by mutableStateOf(selectedFile)
}

@Composable
fun rememberAppState(
    fileStatus: SelectedFile.Status = SelectedFile.Status.LOADING,
    selectedFile: TextFile = TextFile(),
    error: Throwable? = null,
): AppState {
    return remember {
        AppState(
            fileStatus = fileStatus,
            selectedFile = selectedFile,
            error = error
        )
    }
}
