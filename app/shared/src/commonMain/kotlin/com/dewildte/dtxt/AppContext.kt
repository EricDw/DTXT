package com.dewildte.dtxt

import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFileStatus
import com.dewildte.dtxt.utils.Actor

interface AppContext : Actor {
    val showLoading: Boolean
    val backNavigationEnabled: Boolean
    val fileStatus: SelectedFileStatus
    val selectedFile: TextFile
    val error: Throwable?
    val state: AppState
    val stateStack: List<AppState>

    val controller: Actor

    override fun tell(message: Any) {
        /* no-op */
    }
}

interface MutableAppContext: AppContext {
    override var showLoading: Boolean
    override var backNavigationEnabled: Boolean
    override var fileStatus: SelectedFileStatus
    override var selectedFile: TextFile
    override var error: Throwable?
    override var state: AppState
    override var controller: Actor
    override val stateStack: MutableList<AppState>
}