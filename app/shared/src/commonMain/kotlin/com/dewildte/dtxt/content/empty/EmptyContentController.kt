package com.dewildte.dtxt.content.empty

import androidx.compose.runtime.*
import com.dewildte.dtxt.EmptyState

@Composable
fun EmptyContentController(
    state: EmptyState,
) {
    EmptyContent(onEvent = state::tell)
}