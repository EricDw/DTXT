package com.dewildte.dtxt.content.editor

sealed interface EditorContentEvent

data object FindInPageClicked : EditorContentEvent
data object InsertSnippetClicked : EditorContentEvent
data object MoreMenuClicked : EditorContentEvent
data object MoreMenuDismissRequested : EditorContentEvent
data object SelectFileClicked : EditorContentEvent
data object SettingsClicked : EditorContentEvent

data class FileTextChanged(
    val newText: String
) : EditorContentEvent
data class SearchTermChanged(
    val newSearchTerm: String,
) : EditorContentEvent