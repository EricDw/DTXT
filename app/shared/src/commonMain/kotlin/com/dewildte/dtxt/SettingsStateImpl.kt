package com.dewildte.dtxt

import androidx.compose.runtime.Stable
import com.dewildte.dtxt.commands.SetContext
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.commands.TransitionToState
import com.dewildte.dtxt.content.settings.BackClicked
import com.dewildte.dtxt.events.SystemBackButtonClicked

@Stable
class SettingsStateImpl : SettingsState {

    private lateinit var appContext: MutableAppContext
    private lateinit var previousState: AppState

    override fun tell(message: Any) {
        when (message) {
            is SetContext -> {
                previousState = message.context.state
                appContext = message.context
            }
            is Start -> {
                appContext.backNavigationEnabled = previousState != this
                appContext.state = this
            }

            is BackClicked, is SystemBackButtonClicked -> {
                appContext.tell(TransitionToState(previousState))
            }

        }
    }
}