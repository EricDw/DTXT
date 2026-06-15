package com.dewildte.dtxt.queries

import androidx.compose.runtime.Immutable
import com.dewildte.dtxt.data.TextFile

@Immutable
sealed interface SelectedFile {
    @Immutable
    object Loading : SelectedFile
    @Immutable
    data class Loaded(val textFile: TextFile) : SelectedFile
    @Immutable
    object NotFound : SelectedFile

    enum class Status {
        LOADING, LOADED, NOT_FOUND
    }
}