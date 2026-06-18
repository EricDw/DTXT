package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.utils.Actor

@Stable
sealed interface AppState: Actor {
    override fun tell(message: Any) {
        /* no-op */
    }
}

interface InitialState : AppState

interface EmptyState : AppState

interface EditorState : AppState {

    val textFile: TextFile

    val searchMode: Boolean

    val searchTerm: String

    val moreMenuExpanded: Boolean
}

interface SettingsState : AppState