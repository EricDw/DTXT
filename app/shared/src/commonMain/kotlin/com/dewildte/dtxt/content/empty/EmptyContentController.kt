package com.dewildte.dtxt.content.empty

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.utils.Actor

@Composable
@Preview
fun EmptyContentController(
    app: Actor = {},
) {
    EmptyContent { event ->
        when (event) {
            SelectTextFileClicked -> {
                app.tell(SelectTextFile())
            }
        }
    }
}