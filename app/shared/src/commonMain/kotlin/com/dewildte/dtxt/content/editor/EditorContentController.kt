package com.dewildte.dtxt.content.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dewildte.dtxt.AppState
import com.dewildte.dtxt.commands.NavigateToSettings
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.commands.UpdateSelectedFileContent
import com.dewildte.dtxt.utils.Actor

@Composable
fun EditorContentController(
    appState: AppState,
    app: Actor = {},
) {
    val state = rememberEditorContentState(
        textFile = appState.selectedFile
    )

    with(state) {
        EditorContent(
            modifier = Modifier.fillMaxSize(),
            textFile = textFile,
            searchMode = searchMode,
            searchTerm = searchTerm,
            moreMenuExpanded = moreMenuExpanded
        ) { event ->
            when (event) {
                MoreMenuClicked -> {
                    state.moreMenuExpanded = !state.moreMenuExpanded
                }

                FindInPageClicked -> {
                    // TODO: Implement handler
                    state.moreMenuExpanded = false
                }

                InsertSnippetClicked -> {
                    // TODO: Implement handler
                    state.moreMenuExpanded = false
                }

                MoreMenuDismissRequested -> {
                    // TODO: Implement handler
                    state.moreMenuExpanded = false
                }

                SelectFileClicked -> {
                    state.moreMenuExpanded = false
                    app.tell(SelectTextFile())
                }

                SettingsClicked -> {
                    // TODO: Implement handler
                    state.moreMenuExpanded = false
                    app.tell(NavigateToSettings)
                }

                is SearchTermChanged -> {
                    // TODO: Implement handler
                    state.searchTerm = event.newSearchTerm
                }

                is FileTextChanged -> {
                    app.tell(UpdateSelectedFileContent(event.newText))
                }
            }
        }
    }
}