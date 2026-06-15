package com.dewildte.dtxt.components.editor

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.dewildte.dtxt.utils.Actor
import com.dewildte.dtxt.components.editor.events.TextChanged

@Composable
fun Editor(
    modifier: Modifier = Modifier,
    state: EditorState = rememberEditorState(),
    controller: Actor = {}
) {
    val textFieldState = rememberTextFieldState(
        initialText = state.initialText,
    )
    BasicTextField(
        modifier = modifier,
        state = textFieldState,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
    )

    val textFlow = snapshotFlow { textFieldState.text }

    LaunchedEffect(controller) {
        textFlow.collect { newText ->

            controller.tell(TextChanged(newText))
        }
    }
}

@Composable
@Preview
private fun EditorPreview() {
    val editorState = EditorState("Hello World!")
    Editor(state = editorState) { event ->
        when (event) {
            else -> println(event)
        }
    }
}