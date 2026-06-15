package com.dewildte.dtxt.content.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dewildte.dtxt.AppState
import com.dewildte.dtxt.commands.NavigateBack
import com.dewildte.dtxt.utils.Actor

@Composable
fun SettingsContentController(
    appState: AppState,
    app: Actor = {}
) {
    SettingsContent(
        modifier = Modifier.fillMaxSize()
    ) { event ->
        when (event) {
            BackClicked -> {
                app.tell(NavigateBack)
            }
        }
    }
}