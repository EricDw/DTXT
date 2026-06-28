package com.dewildte.dtxt.utils

import com.dewildte.dtxt.commands.LogMessage
import com.dewildte.dtxt.data.LogData
import com.dewildte.dtxt.data.LogLevel

fun Actor.tellVerboseLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.VERBOSE,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}

fun Actor.tellDebugLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.DEBUG,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}

fun Actor.tellInfoLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.INFO,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}

fun Actor.tellWarningLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.WARN,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}

fun Actor.tellErrorLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.ERROR,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}

fun Actor.tellWtfLog(tag: String, message: String, error: Throwable? = null) {
    val logData = LogData(
        level = LogLevel.WTF,
        tag = tag,
        message = message,
        error = error,
    )
    tell(LogMessage(logData))
}