package com.maproductions.lib.mautils_gson_core

internal inline fun runSafely(block: () -> Unit) {
    try {
        block()
    }catch (e: Throwable) {
        // ignore
    }
}

internal inline fun <T> T.runSafely(block: T.() -> Unit) {
    try {
        block()
    }catch (e: Throwable) {
        // ignore
    }
}
