package com.dewildte.dtxt.commands

import com.dewildte.dtxt.data.LogData

data class LogMessage(
    val logData: LogData
): Command
