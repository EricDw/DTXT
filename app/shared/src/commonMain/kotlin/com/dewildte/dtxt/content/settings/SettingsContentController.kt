package com.dewildte.dtxt.content.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dewildte.dtxt.SettingsState

@Composable
fun SettingsContentController(
    state: SettingsState
) {
    SettingsContent(
        modifier = Modifier.fillMaxSize(),
        onEvent = state::tell
    )
}