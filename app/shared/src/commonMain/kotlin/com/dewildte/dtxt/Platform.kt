package com.dewildte.dtxt

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform