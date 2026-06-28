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
    private lateinit var context: MutableAppContext

    override fun tell(message: Any) {
        when (message) {
            is SetContext -> {
                context = message.context
            }

            is Start -> {
                context.run {
                    showLoading = true
                    controller.tell(LoadSelectedFile())
                }
            }

            is FileSelected -> {
                context.tell(
                    TransitionToState(
                        EditorStateImpl(
                            textFile = message.textFile
                        )
                    )
                )
            }

            is FailedToLoadSelectedFile -> {
                context.tell(
                    TransitionToState(
                        EmptyStateImpl()
                    )
                )
            }
        }
    }
}