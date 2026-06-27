package com.dewildte.dtxt.content.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.utils.samples.SampleText
import dtxt.app.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditorContent(
    modifier: Modifier = Modifier,
    textFile: TextFile = TextFile(),
    searchMode: Boolean = false,
    searchTerm: String = "",
    moreMenuExpanded: Boolean = false,
    onEvent: (EditorContentEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize().imePadding(),
        topBar = {
            TopBar(
                textFile = textFile,
                moreMenuExpanded = moreMenuExpanded,
                onEvent = onEvent,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (searchMode) {
                val searchTermFieldState = rememberTextFieldState(
                    searchTerm
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
                        onEvent(SearchTermChanged(it.toString()))
                    }
                }
            }

            val textFieldState = rememberTextFieldState(
                initialText = textFile.contents,
            )
            BasicTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                state = textFieldState,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            )

            val textFlow = snapshotFlow { textFieldState.text }

            LaunchedEffect(onEvent) {
                textFlow.collect { newText ->
                    onEvent(FileTextChanged(newText.toString()))
                }
            }

            LaunchedEffect(textFile.path) {
                textFieldState.setTextAndPlaceCursorAtEnd(textFile.contents)
            }
        }
    }
}

@Composable
private fun TopBar(
    textFile: TextFile,
    moreMenuExpanded: Boolean,
    onEvent: (EditorContentEvent) -> Unit,
) {
    TopAppBar(
        title = { Text(textFile.path) },
        actions = {
            Box {
                IconButton(
                    onClick = {
                        onEvent(MoreMenuClicked)
                    },
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(
                    expanded = moreMenuExpanded,
                    onDismissRequest = {
                        onEvent(MoreMenuDismissRequested)
                    },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.label_select_file)) },
                        onClick = {
                            onEvent(SelectFileClicked)
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
                            onEvent(InsertSnippetClicked)
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
                            onEvent(FindInPageClicked)
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
                            onEvent(SettingsClicked)
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
}

@Composable
@Preview(showBackground = true)
private fun EditorContentPreview() {
    EditorContent(
        modifier = Modifier.fillMaxSize(),
        textFile = TextFile(
            path = SampleText.textFileName,
            contents = SampleText.loremIpsum
        ),
        searchTerm = "",
        moreMenuExpanded = true,
    )
}