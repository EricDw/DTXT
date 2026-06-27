package com.dewildte.dtxt

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import com.dewildte.dtxt.commands.*
import com.dewildte.dtxt.data.LogData
import com.dewildte.dtxt.data.LogLevel
import com.dewildte.dtxt.data.TextFile
import com.dewildte.dtxt.events.FailedToLoadSelectedFile
import com.dewildte.dtxt.events.FailedToSelectFile
import com.dewildte.dtxt.events.FailedToUpdateFileContent
import com.dewildte.dtxt.events.FileSelected
import com.dewildte.dtxt.events.SystemBackButtonClicked
import com.dewildte.dtxt.utils.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), Actor {

    private val appContext: AppContextImpl = AppContextImpl(
        controller = this
    )
    private val filePicker = registerForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
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
                appContext.tell(FailedToSelectFile())
            }
        } catch (cause: Throwable) {
            appContext.tell(FailedToSelectFile(cause))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val colorScheme = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(this@MainActivity)
            } else {
                dynamicLightColorScheme(this@MainActivity)
            }

            MaterialTheme(
                colorScheme = colorScheme,
            ) {
                App(appContext = appContext)
            }

            BackHandler(enabled = appContext.backNavigationEnabled) {
                appContext.tell(SystemBackButtonClicked)
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
                        appContext.state.tell(FailedToLoadSelectedFile())
                    }
                } catch (cause: Throwable) {
                    appContext.tell(FailedToSelectFile(cause))
                }
            }

            is SelectTextFile -> {
                try {
                    filePicker.launch(arrayOf("text/plain"))
                } catch (e: Throwable) {
                    appContext.tell(FailedToSelectFile(e))
                }
            }

            is UpdateSelectedFileContent -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    logMessage(
                        LogData(
                            level = LogLevel.DEBUG,
                            tag = TAG,
                            message = message.newContent.toString()
                        )
                    )
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
                    } catch (cause: Throwable) {
                        appContext.tell(FailedToUpdateFileContent(cause))
                    }
                }
            }

            else -> {
                logMessage(
                    LogData(
                        level = LogLevel.DEBUG,
                        tag = TAG,
                        message = "MESSAGE: $message"
                    )
                )
            }
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
            val textFile = TextFile(
                path = path,
                contents = contents,
            )

            appContext.state.tell(FileSelected(textFile))

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