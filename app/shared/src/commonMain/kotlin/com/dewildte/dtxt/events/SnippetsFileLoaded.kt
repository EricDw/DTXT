package com.dewildte.dtxt.events

import com.dewildte.dtxt.data.TextFile

data class SnippetsFileLoaded(
    val file: TextFile
): Event