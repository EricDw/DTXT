package com.dewildte.dtxt.content.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.components.editor.Editor
import com.dewildte.dtxt.components.editor.rememberEditorState
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.utils.Actor
import com.dewildte.dtxt.utils.samples.SampleText
import dtxt.app.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditorContent(
    modifier: Modifier = Modifier,
    state: EditorContentState = rememberEditorContentState(),
    controller: Actor = {},
) {
    val editorState = rememberEditorState(state.textFile.contents)

    Scaffold(
        modifier = modifier.fillMaxSize().imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(state.textFile.path) },
                actions = {
                    Box {
                        IconButton(onClick = { state.moreMenuExpanded = !state.moreMenuExpanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = state.moreMenuExpanded,
                            onDismissRequest = { state.moreMenuExpanded = !state.moreMenuExpanded },
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.label_select_file)) },
                                onClick = {
                                    state.moreMenuExpanded = !state.moreMenuExpanded
                                    controller.tell(SelectTextFile())
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FileOpen,
                                        contentDescription = stringResource(Res.string.desc_select_file)
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.label_insert_snippet)) },
                                onClick = {
                                    state.moreMenuExpanded = !state.moreMenuExpanded
                                    // TODO: Open snippet selector
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.TextSnippet,
                                        contentDescription = stringResource(Res.string.desc_insert_snippet)
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.label_find_in_page)) },
                                onClick = {
                                    state.moreMenuExpanded = !state.moreMenuExpanded
                                    state.searchMode = !state.searchMode
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FindInPage,
                                        contentDescription = stringResource(Res.string.desc_find_in_page)
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.label_settings)) },
                                onClick = {
                                    state.moreMenuExpanded = !state.moreMenuExpanded
                                    // TODO: Navigate to settings
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = stringResource(Res.string.desc_settings)
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.searchMode) {
                val searchTermFieldState = rememberTextFieldState(
                    state.searchTerm
                )
                OutlinedTextField(
                    state = searchTermFieldState,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                val searchTermFlow = remember(searchTermFieldState) {
                    snapshotFlow { searchTermFieldState.text }
                }

                LaunchedEffect(searchTermFieldState) {
                    searchTermFlow.collect {
                        state.searchTerm = it.toString()
                    }
                }
            }

            Editor(
                modifier = Modifier.weight(1F)
                    .padding(8.dp),
                state = editorState,
            ) { event ->
                when (event) {
                    else -> controller.tell(event)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EditorContentPreview() {
    val editorState = EditorContentState(
        textFile = TextFile(
            path = SampleText.textFileName,
            contents = SampleText.loremIpsum
        ),
        contextMenuExpanded = true,
    )
    EditorContent(
        modifier = Modifier.fillMaxSize(),
        state = editorState,
    )
}