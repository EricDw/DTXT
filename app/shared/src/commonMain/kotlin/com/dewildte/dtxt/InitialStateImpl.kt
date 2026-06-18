package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import com.dewildte.dtxt.commands.LoadSelectedFile
import com.dewildte.dtxt.commands.SetContext
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.commands.TransitionToState
import com.dewildte.dtxt.events.FailedToLoadSelectedFile
import com.dewildte.dtxt.events.FileSelected

@Stable
class InitialStateImpl : InitialState {
    private lateinit var appContext: MutableAppContext

    override fun tell(message: Any) {
        when (message) {
            is SetContext -> {
                appContext = message.context
            }

            is Start -> {
                appContext.run {
                    showLoading = true
                    controller.tell(LoadSelectedFile())
                }
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

            is FailedToLoadSelectedFile -> {
                tell(
                    TransitionToState(
                        EmptyStateImpl()
                    )
                )
            }
        }
    }
}