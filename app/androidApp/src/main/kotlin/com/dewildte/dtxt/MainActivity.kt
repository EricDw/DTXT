package com.dewildte.dtxt

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.dewildte.dtxt.commands.LoadSelectedFile
import com.dewildte.dtxt.commands.LogMessage
import com.dewildte.dtxt.commands.SelectTextFile
import com.dewildte.dtxt.commands.UpdateSelectedFileContent
import com.dewildte.dtxt.data.LogData
import com.dewildte.dtxt.data.LogLevel
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.queries.SelectedFile
import com.dewildte.dtxt.utils.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), Actor {

    private val appState: AppState = AppState()
    private val filePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        try {
            if (uri != null) {
                Log.d(TAG, "Uri:\n$uri")
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                getPreferences(MODE_PRIVATE).edit {
                    putString(KEY_SELECTED_FILE_URI, uri.toString())
                }

                onSelectedFileUriFound(uri)

            } else {
                if (appState.selectedFile.path.isNotEmpty())
                    appState.apply {
                        fileStatus = SelectedFile.Status.LOADED
                    }
                /**
                 * Given the user has a file they were previously editing.
                 * When they then back out of selecting a new one.
                 * Then do nothing.
                 */
            }
        } catch (t: Throwable) {
            appState.apply {
                fileStatus = SelectedFile.Status.NOT_FOUND
                selectedFile = TextFile()
                error = t
            }

            println(t.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            val colorScheme = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                dynamicLightColorScheme(LocalContext.current)
            }

            MaterialTheme(
                colorScheme = colorScheme,
            ) {
                App(state = appState, controller = this@MainActivity)
            }
        }
    }

    override fun tell(message: Any) {
        when (message) {
            is LogMessage -> {
                logMessage(message.logData)
            }

            is LoadSelectedFile -> {
                try {
                    val uri = getPreferences(MODE_PRIVATE)
                        .getString(
                            KEY_SELECTED_FILE_URI,
                            null
                        )
                        ?.toUri()

                    if (uri != null) {
                        onSelectedFileUriFound(uri)
                    } else {
                        getPreferences(MODE_PRIVATE).edit {
                            putString(KEY_SELECTED_FILE_URI, null)
                        }
                        appState.apply {
                            fileStatus = SelectedFile.Status.NOT_FOUND
                            selectedFile = TextFile()
                        }
                    }
                } catch (e: Throwable) {
                    appState.apply {
                        selectedFile = TextFile()
                    }
                    println(e.message)
                }
            }

            is SelectTextFile -> {
                try {
                    appState.apply {
                        fileStatus = SelectedFile.Status.LOADING
                    }
                    filePicker.launch(arrayOf("text/plain"))
                } catch (e: Throwable) {
                    println(e.message)
                }
            }

            is UpdateSelectedFileContent -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.d(TAG, message.newContent.toString())
                    try {

                        val uri = getPreferences(MODE_PRIVATE)
                            .getString(
                                KEY_SELECTED_FILE_URI,
                                null
                            )
                            ?.toUri()

                        if (uri != null) {

                            contentResolver.openOutputStream(uri, "wt")?.use { stream ->
                                stream.write(message.newContent.toString().toByteArray())
                                stream.flush()
                            }

                        } else {
                            getPreferences(MODE_PRIVATE).edit {
                                putString(KEY_SELECTED_FILE_URI, null)
                            }
                        }
                    } catch (e: Throwable) {
                        appState.apply {
                            error = e
                        }
                    }
                }
            }

            else -> println(message)
        }

    }

    private fun logMessage(message: LogData) {
        when (message.level) {
            LogLevel.VERBOSE -> {
                Log.v(TAG, message.message, message.error)
            }
            LogLevel.DEBUG -> {
                Log.d(TAG, message.message, message.error)
            }
            LogLevel.INFO -> {
                Log.i(TAG, message.message, message.error)
            }
            LogLevel.WARN -> {
                Log.w(TAG, message.message, message.error)
            }
            LogLevel.ERROR -> {
                Log.e(TAG, message.message, message.error)
            }
            LogLevel.WTF -> {
                Log.wtf(TAG, message.message, message.error)
            }
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        val selectedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString() ?: ""
        Log.d(TAG, selectedText)
    }

    private fun onSelectedFileUriFound(uri: Uri) {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val contents = inputStream.reader().readText()
            val path = uri.lastPathSegment?.replace("primary:", "") ?: ""
            val textFileData = TextFile(
                path = path,
                contents = contents,
            )

            appState.apply {
                fileStatus = SelectedFile.Status.LOADED
                selectedFile = textFileData
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_SELECTED_FILE_URI = "key_selected_file_uri"
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}