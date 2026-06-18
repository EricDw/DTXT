package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.commands.SetContext
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.commands.TransitionToState
import com.dewildte.dtxt.content.empty.SelectTextFileClicked
import com.dewildte.dtxt.events.FileSelected
import com.dewildte.dtxt.queries.SelectedFileStatus

@Stable
class EmptyStateImpl : EmptyState {

    private lateinit var appContext: MutableAppContext

    override fun tell(message: Any) {
        when (message) {
            is SetContext -> {
                appContext = message.context
            }

            is Start -> {
                appContext.apply {
                    backNavigationEnabled = false
                    showLoading = false
                    state = this@EmptyStateImpl
                }
            }

            is SelectTextFileClicked -> {
                appContext.controller.tell(SelectTextFile())
            }

            is FileSelected -> {
                appContext.apply {
                    fileStatus = SelectedFileStatus.LOADED
                    selectedFile = message.textFile

                }.tell(
                    TransitionToState(
                        EditorStateImpl(
                            textFile = message.textFile
                        )
                    )
                )
            }
        }
    }
}