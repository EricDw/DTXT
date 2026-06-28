package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dewildte.dtxt.commands.*
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.events.EditorContentEvent
import com.dewildte.dtxt.events.FailedToLoadSelectedFile
import com.dewildte.dtxt.events.FailedToLoadSelectedSnippetsFile
import com.dewildte.dtxt.events.FailedToSelectSnippetsFile
import com.dewildte.dtxt.events.FailedToUpdateFileContent
import com.dewildte.dtxt.events.FileSelected
import com.dewildte.dtxt.events.FileTextChanged
import com.dewildte.dtxt.events.FindInPageClicked
import com.dewildte.dtxt.events.InsertSnippetClicked
import com.dewildte.dtxt.events.MoreMenuClicked
import com.dewildte.dtxt.events.MoreMenuDismissRequested
import com.dewildte.dtxt.events.SearchTermChanged
import com.dewildte.dtxt.events.SelectFileClicked
import com.dewildte.dtxt.events.SelectSnippetsFileClicked
import com.dewildte.dtxt.events.SettingsClicked
import com.dewildte.dtxt.events.SnippetClicked
import com.dewildte.dtxt.events.SnippetInserted
import com.dewildte.dtxt.events.SnippetSelectorDismissRequested
import com.dewildte.dtxt.events.SnippetsFileLoaded
import com.dewildte.dtxt.events.SnippetsFileSelected
import com.dewildte.dtxt.queries.GetCurrentDateString
import com.dewildte.dtxt.utils.tellErrorLog
import kotlin.time.Clock

@Stable
class EditorStateImpl(
    textFile: TextFile = TextFile(),
    searchMode: Boolean = false,
    searchTerm: String = "",
    moreMenuExpanded: Boolean = false,
    snippetSelectorExpanded: Boolean = false,
    snippetSelectorIsEmpty: Boolean = true,
    snippets: List<String> = emptyList()
) : EditorState {

    private lateinit var context: MutableAppContext
    private lateinit var previousState: AppState

    override var textFile: TextFile by mutableStateOf(textFile)
    override var searchMode: Boolean by mutableStateOf(searchMode)
    override var searchTerm: String by mutableStateOf(searchTerm)
    override var moreMenuExpanded: Boolean by mutableStateOf(moreMenuExpanded)
    override var snippetSelectorExpanded: Boolean by mutableStateOf(snippetSelectorExpanded)
    override var snippetSelectorIsEmpty: Boolean by mutableStateOf(snippetSelectorIsEmpty)
    override val snippets: MutableList<String> = mutableStateListOf(*snippets.toTypedArray())

    override var snippetToInsert: String? by mutableStateOf(null)
    private var snippetsFile: TextFile? = null

    override fun tell(message: Any) {
        when (message) {

            is SetContext -> {
                context = message.context
                previousState = context.state
            }

            is Start -> {
                context.apply {
                    backNavigationEnabled = false
                    state = this@EditorStateImpl
                    showLoading = false
                }

                if (snippetsFile == null)
                    context.controller.tell(LoadSnippetsFile)
            }

            is EditorContentEvent -> {
                handleEditorContentEvent(message)
            }

            is FileSelected -> {
                textFile = message.textFile
            }

            is SnippetsFileSelected -> {
                snippetsFile = message.textFile
                val newSnippets = message.textFile.contents.split("\n\n\n")
                snippets.clear()
                snippets.addAll(newSnippets)
                snippetSelectorIsEmpty = false
            }

            is SnippetsFileLoaded -> {
                snippetsFile = message.file
                val newSnippets = message.file.contents.split("\n\n\n")
                snippets.clear()
                snippets.addAll(newSnippets)
                snippetSelectorIsEmpty = false
            }

            is FailedToUpdateFileContent -> {
                context.controller.tellErrorLog(
                    tag = TAG,
                    message = message.toString(),
                    error = message.cause,
                )
            }

            is FailedToLoadSelectedFile -> {
                context.controller.tellErrorLog(
                    tag = TAG,
                    message = message.toString(),
                    error = message.cause,
                )
            }

            is FailedToLoadSelectedSnippetsFile -> {
                snippetSelectorIsEmpty = true
            }

            is FailedToSelectSnippetsFile -> {
                snippetSelectorIsEmpty = true
            }
        }
    }

    private fun handleEditorContentEvent(event: EditorContentEvent) {
        when (event) {

            is MoreMenuClicked -> {
                moreMenuExpanded = !moreMenuExpanded
            }

            is FindInPageClicked -> {
                moreMenuExpanded = false
            }

            is InsertSnippetClicked -> {
                moreMenuExpanded = false
                snippetSelectorExpanded = true
                context.controller.tell(LoadSnippetsFile)
            }

            is MoreMenuDismissRequested -> {
                moreMenuExpanded = false
            }

            is SelectFileClicked -> {
                moreMenuExpanded = false
                context.controller.tell(SelectTextFile())
            }

            is SettingsClicked -> {
                moreMenuExpanded = false
                context.tell(TransitionToState(SettingsStateImpl()))
            }

            is SearchTermChanged -> {
                searchTerm = event.newSearchTerm
            }

            is FileTextChanged -> {
                val (newText) = event
                if (textFile.contents != newText) {
                    context.controller.tell(UpdateSelectedFileContent(newText))
                    textFile = textFile.copy(contents = newText)
                }
            }

            is SnippetSelectorDismissRequested -> {
                snippetSelectorExpanded = false
            }

            is SelectSnippetsFileClicked -> {
                context.controller.tell(SelectSnippetsFile())
            }

            is SnippetClicked -> {
                // TODO: Replace $DATE$ with date
                val (snippet) = event
                context.controller.tell(
                    GetCurrentDateString { dateString ->
                        snippetToInsert = snippet.replace($$"$DATE$", dateString)
                        snippetSelectorExpanded = false
                    }
                )

            }

            is SnippetInserted -> {
                snippetToInsert = null
            }
        }
    }

    companion object {
        private const val TAG = "EditorState"
    }
}