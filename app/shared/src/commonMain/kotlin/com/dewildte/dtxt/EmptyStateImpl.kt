package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.commands.SetContext
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.commands.TransitionToState
import com.dewildte.dtxt.content.empty.SelectTextFileClicked
import com.dewildte.dtxt.events.FileSelected

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
                appContext.tell(
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