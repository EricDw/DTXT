package com.dewildte.dtxt

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dewildte.dtxt.commands.Start
import com.dewildte.dtxt.content.editor.EditorContentController
import com.dewildte.dtxt.content.empty.EmptyContentController
import com.dewildte.dtxt.content.loading.LoadingContent
import com.dewildte.dtxt.content.settings.SettingsContentController
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.utils.samples.SampleText

@Composable
fun App(
    appContext: AppContext = rememberAppContext(),
) {

    when (val state = appContext.state) {
        is EditorState -> {
            EditorContentController(
                state = state
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
            SettingsContentController(
                state = state
            )
        }
    }

    if (appContext.showLoading) {
        LoadingContent()
    }
}

class AppSettingsContextPreviewParameterProvider : PreviewParameterProvider<AppContext> {

    val settingsContext = AppContextImpl(
        showLoading = false,
        state = SettingsStateImpl()
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
    override val values: Sequence<AppContext>
        get() = sequenceOf(
            editorContext,
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
