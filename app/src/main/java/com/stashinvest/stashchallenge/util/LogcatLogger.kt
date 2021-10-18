package com.stashinvest.stashchallenge.util

import android.util.Log
import com.stashinvest.stashchallenge.util.ILogger
import com.stashinvest.stashchallenge.util.ILogger.LogLevel
import com.stashinvest.stashchallenge.util.ILogger.LogLevel.*
import javax.inject.Inject

class LogcatLogger @Inject constructor() : ILogger {

    override fun log(tag: String?, message: String?, error: Throwable?, logLevel: LogLevel) {
        when (logLevel) {
            VERBOSE -> Log.v(tag, message, error)
            DEBUG -> Log.d(tag, message, error)
            ERROR -> Log.e(tag, message, error)
            WARNING -> Log.w(tag, message, error)
            INFORMATION -> Log.i(tag, message, error)
        }
    }
}