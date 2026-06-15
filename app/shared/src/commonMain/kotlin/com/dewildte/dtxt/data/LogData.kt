package com.dewildte.dtxt.data

data class LogData(
    val level: LogLevel,
    val message: String,
    val error: Error? = null,
)
