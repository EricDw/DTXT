package com.dewildte.dtxt

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.dewildte.dtxt.commands.LoadSelectedFile
import com.dewildte.dtxt.commands.NavigateBack
import com.dewildte.dtxt.commands.NavigateToSettings
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.content.ContentType
import com.dewildte.dtxt.content.editor.EditorContentController
import com.dewildte.dtxt.content.empty.EmptyContentController
import com.dewildte.dtxt.content.empty.SelectTextFileClicked
import com.dewildte.dtxt.content.loading.LoadingContent
import com.dewildte.dtxt.content.settings.SettingsContentController
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFile
import com.dewildte.dtxt.utils.Actor

@Composable
fun App(
    state: AppState = rememberAppState(),
    controller: Actor = {},
) {

    when (val contentType = state.contentType) {
        ContentType.EDITOR -> {
            when (state.fileStatus) {
                SelectedFile.Status.LOADED -> {
                    EditorContentController(
                        appState = state,
                    ) { message ->
                        when (message) {
                            NavigateToSettings -> {
                                if (contentType != ContentType.SETTINGS) {
                                    state.contentType = ContentType.SETTINGS
                                }
                            }

                            else -> controller.tell(message)
                        }
                    }
                }

                SelectedFile.Status.LOADING -> {
                    LoadingContent()
                }

                SelectedFile.Status.NOT_FOUND -> {
                    EmptyContentController { message ->
                        when (message) {
                            is SelectTextFile -> {
                                controller.tell(message)
                            }
                        }
                    }
                }
            }
        }

        ContentType.SETTINGS -> {
            SettingsContentController(
                appState = state,
            ) { message ->
                when (message) {
                    NavigateBack -> {
                        if (contentType != ContentType.EDITOR) {
                            state.contentType = ContentType.EDITOR
                        }
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