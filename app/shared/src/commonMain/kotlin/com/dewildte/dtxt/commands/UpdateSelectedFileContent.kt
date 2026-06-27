package com.dewildte.dtxt.commands

data class UpdateSelectedFileContent(
    val newContent: CharSequence,
): Command