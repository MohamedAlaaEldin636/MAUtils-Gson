package com.maproductions.lib.mautilsgson

import android.app.Application
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
    }

    private class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }

}
