package com.aking.base.widget

import android.util.Log
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * 文件日志记录树
 * @author Ak
 * 2024/8/17 10:30
 */
class FileLoggingTree(private val logFile: File, private val enable: Boolean = false) : Timber.Tree() {

    constructor(logPath: String) : this(File(logPath))

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (enable.not()) return

        BufferedWriter(FileWriter(logFile, true)).use {
            it.write(String.format("%s: %s\n", tag, message))
            if (t != null) {
                it.write(Log.getStackTraceString(t))
            }
        }
    }
}
