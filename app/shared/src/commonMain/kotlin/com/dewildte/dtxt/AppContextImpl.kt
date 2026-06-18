package com.dewildte.dtxt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dewildte.dtxt.commands.SetContext
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.commands.TransitionToState
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFileStatus
import com.dewildte.dtxt.utils.Actor

@Stable
class AppContextImpl(
    fileStatus: SelectedFileStatus = SelectedFileStatus.LOADING,
    selectedFile: TextFile = TextFile.EMPTY,
    error: Throwable? = null,
    showLoading: Boolean = true,
    state: AppState = InitialStateImpl(),
    controller: Actor = {},
) : MutableAppContext, Actor {

    override var showLoading: Boolean by mutableStateOf(showLoading)
    override var backNavigationEnabled: Boolean by mutableStateOf(false)
    override var fileStatus: SelectedFileStatus by mutableStateOf(fileStatus)
    override var selectedFile: TextFile by mutableStateOf(selectedFile)
    override var error: Throwable? by mutableStateOf(error)
    override var state: AppState by mutableStateOf(state)
    override var stateStack: MutableList<AppState> = mutableStateListOf(state)
    override var controller: Actor by mutableStateOf(controller)

    override fun tell(message: Any) {
        when (message) {
            is Start -> {
                state.tell(SetContext(this))
                state.tell(Start)
            }

            is TransitionToState -> {
                message.newState.tell(SetContext(this))
                message.newState.tell(Start)
            }

            else -> state.tell(message)
        }
    }

}

@Composable
fun rememberAppContext(
    fileStatus: SelectedFileStatus = SelectedFileStatus.LOADING,
    selectedFile: TextFile = TextFile.EMPTY,
    error: Throwable? = null,
    state: AppState = InitialStateImpl(),
    controller: Actor = {}
): AppContextImpl {
    return remember {
        AppContextImpl(
            fileStatus = fileStatus,
            selectedFile = selectedFile,
            error = error,
            state = state,
            controller = controller,
        )
    }
}

