package com.dewildte.dtxt.data

data class LogData(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val error: Throwable? = null,
)
