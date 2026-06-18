package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dewildte.dtxt.commands.*
import com.dewildte.dtxt.content.editor.*
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.events.FailedToUpdateFileContent

@Stable
class EditorStateImpl(
    textFile: TextFile = TextFile(),
    searchMode: Boolean = false,
    searchTerm: String = "",
    moreMenuExpanded: Boolean = false,
) : EditorState {

    override var textFile: TextFile by mutableStateOf(textFile)

    override var searchMode: Boolean by mutableStateOf(searchMode)

    override var searchTerm: String by mutableStateOf(searchTerm)

    override var moreMenuExpanded: Boolean by mutableStateOf(moreMenuExpanded)

    private lateinit var appContext: MutableAppContext
    private lateinit var previousState: AppState

    override fun tell(message: Any) {
        when (message) {

            is SetContext -> {
                appContext = message.context
                previousState = appContext.state
            }

            is Start -> {
                appContext.apply {
                    backNavigationEnabled = previousState !is EmptyState
                    state = this@EditorStateImpl
                    showLoading = false
                }
            }

            is MoreMenuClicked -> {
                moreMenuExpanded = !moreMenuExpanded
            }

            is FindInPageClicked -> {
                // TODO: Implement handler
                moreMenuExpanded = false
            }

            is InsertSnippetClicked -> {
                // TODO: Implement handler
                moreMenuExpanded = false
            }

            is MoreMenuDismissRequested -> {
                moreMenuExpanded = false
            }

            is SelectFileClicked -> {
                moreMenuExpanded = false
                appContext.controller.tell(SelectTextFile())
            }

            is SettingsClicked -> {
                moreMenuExpanded = false
                appContext.tell(TransitionToState(SettingsStateImpl()))
            }

            is SearchTermChanged -> {
                // TODO: Implement handler
                searchTerm = message.newSearchTerm
            }

            is FileTextChanged -> {
                val (newText) = message
                if (textFile.contents != newText) {
                    appContext.controller.tell(UpdateSelectedFileContent(newText))
                    textFile = textFile.copy(contents = newText)
                }
            }

            is FailedToUpdateFileContent -> {
                println(message)
            }
        }
    }
}