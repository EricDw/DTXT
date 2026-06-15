package com.dewildte.dtxt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dewildte.dtxt.commands.LoadSelectedFile
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.commands.UpdateSelectedFileContent
import com.dewildte.dtxt.components.editor.events.TextChanged
import com.dewildte.dtxt.content.editor.EditorContent
import com.dewildte.dtxt.content.editor.rememberEditorContentState
import com.dewildte.dtxt.content.empty.EmptyContent
import com.dewildte.dtxt.content.empty.events.SelectTextFileClick
import com.dewildte.dtxt.content.loading.LoadingContent
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFile
import com.dewildte.dtxt.utils.Actor

@Composable
fun App(
    modifier: Modifier = Modifier,
    state: AppState = rememberAppState(),
    controller: Actor = {},
) {
    when (state.fileStatus) {
        SelectedFile.Status.LOADED -> {
            val editorState = rememberEditorContentState(
                state.selectedFile,
            )
            EditorContent(
                modifier = modifier,
                state = editorState,
            ) { event ->
                when (event) {
                    is TextChanged -> {
                        controller.tell(UpdateSelectedFileContent(event.newText))
                    }

                    else -> controller.tell(event)
                }
            }
        }

        SelectedFile.Status.LOADING -> {
            LoadingContent()
        }

        SelectedFile.Status.NOT_FOUND -> {
            EmptyContent { event ->
                when (event) {
                    is SelectTextFileClick -> {
                        controller.tell(SelectTextFile())
                    }
                }
            }
        }
    }

    LaunchedEffect(controller) {
        controller.tell(LoadSelectedFile())
    }
}

@Composable
@Preview
private fun AppLoadingPreview() {
    App()
}

@Composable
@Preview
private fun AppLoadedPreview() {
    val state = rememberAppState(
        fileStatus = SelectedFile.Status.LOADED,
        selectedFile = TextFile("Preview.txt", "Hello World!")
    )
    App(state = state)
}

@Composable
@Preview
private fun AppEmptyPreview() {
    val state = rememberAppState(
        fileStatus = SelectedFile.Status.NOT_FOUND,
    )
    App(state = state)
}