package com.dewildte.dtxt.content.empty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dewildte.dtxt.utils.Actor
import com.dewildte.dtxt.content.empty.events.SelectTextFileClick

@Composable
@Preview
fun EmptyContent(
    controller: Actor = {},
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            TextButton(onClick = { controller.tell(SelectTextFileClick()) }) {
                Text("Select .txt File")
            }
        }
    }
}