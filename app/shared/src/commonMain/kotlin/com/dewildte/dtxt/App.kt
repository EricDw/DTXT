package com.dewildte.dtxt

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.components.EditorBottomSheet
import com.dewildte.dtxt.components.EditorTopBar
import com.dewildte.dtxt.components.SettingsTopBar
import com.dewildte.dtxt.content.editor.EditorContent
import com.dewildte.dtxt.content.empty.EmptyContentController
import com.dewildte.dtxt.content.loading.LoadingContent
import com.dewildte.dtxt.content.settings.SettingsContent
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.utils.samples.SampleSnippets
import com.dewildte.dtxt.utils.samples.SampleText

@Composable
fun App(
    appContext: AppContext = rememberAppContext(),
) {

    val state = appContext.state

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when (state) {
                is EditorState -> {
                    EditorTopBar(state = state)
                }

                is EmptyState -> {
                    // TODO: Implement
                }

                is InitialState -> {
                    // TODO: Implement
                }

                is SettingsState -> {
                    SettingsTopBar(state = state)
                }
            }
        },
    ) { innerPadding ->

        when (state) {
            is EditorState -> {
                EditorContent(
                    state = state,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is EmptyState -> {
                EmptyContentController(
                    state = state
                )
            }

            is InitialState -> {
                appContext.tell(Start)
            }

            is SettingsState -> {
                SettingsContent(
                    state = state,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

        if (appContext.showLoading) {
            LoadingContent()
        }
    }

    //region Bottom Sheet
    when (state) {
        is EditorState -> {
            EditorBottomSheet(state = state)
        }

        else -> {
            /* no-op */
        }
    }
    //endregion Bottom Sheet
}

class AppSettingsContextPreviewParameterProvider : PreviewParameterProvider<AppContext> {

    val settingsContext = AppContextImpl(
        showLoading = false,
        state = SettingsStateImpl(
            snippets = SampleSnippets.basic10,
        )
    )

    override val values: Sequence<AppContext>
        get() = sequenceOf(
            settingsContext,
        )
}

class AppEditorContextPreviewParameterProvider : PreviewParameterProvider<AppContext> {

    val editorContext = AppContextImpl(
        showLoading = false,
        state = EditorStateImpl(
            textFile = TextFile(
                path = SampleText.textFileName,
                contents = SampleText.loremIpsum,
            )
        )
    )

    val editorMenuContext = AppContextImpl(
        showLoading = false,
        state = EditorStateImpl(
            textFile = TextFile(
                path = SampleText.textFileName,
                contents = SampleText.loremIpsum,
            ),
            moreMenuExpanded = true
        )
    )

    val editorSnippetSelectorContext = AppContextImpl(
        showLoading = false,
        state = EditorStateImpl(
            textFile = TextFile(
                path = SampleText.textFileName,
                contents = SampleText.loremIpsum,
            ),
            snippetSelectorExpanded = true,
            snippets = SampleSnippets.basic10,
        )
    )

    override val values: Sequence<AppContext>
        get() = sequenceOf(
            editorContext,
            editorMenuContext,
            editorSnippetSelectorContext,
        )
}

@Composable
@Preview
private fun LoadingPreview() {
    App(appContext = AppContextImpl())
}

@Composable
@Preview
private fun AppEmptyPreview() {
    val appContext = AppContextImpl(
        showLoading = false,
        state = EmptyStateImpl()
    )
    App(appContext = appContext)
}

@Composable
@Preview
private fun AppEditorPreview(
    @PreviewParameter(AppEditorContextPreviewParameterProvider::class)
    appContext: AppContext
) {
    App(appContext = appContext)
}

@Composable
@Preview
private fun AppSettingsPreview(
    @PreviewParameter(AppSettingsContextPreviewParameterProvider::class)
    appContext: AppContext,
) {
    App(appContext = appContext)
}
