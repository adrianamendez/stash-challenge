package com.stashinvest.stashchallenge.api.domain

interface ILogger {

    enum class LogLevel {
        VERBOSE, DEBUG, ERROR, WARNING, INFORMATION
    }

    fun log(tag: String?, message: String?, error: Throwable?, logLevel: LogLevel)
}