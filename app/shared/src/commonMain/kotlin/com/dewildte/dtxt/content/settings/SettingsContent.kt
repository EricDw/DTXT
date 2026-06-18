package com.dewildte.dtxt.content.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dtxt.app.shared.generated.resources.Res
import dtxt.app.shared.generated.resources.desc_navigate_back
import dtxt.app.shared.generated.resources.label_settings
import dtxt.app.shared.generated.resources.title_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onEvent: (SettingsContentEvent) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.title_settings),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(BackClicked)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.desc_navigate_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("🏗️ Under Construction", style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
@Preview
private fun SettingsContentPreview() {
    SettingsContent()
}