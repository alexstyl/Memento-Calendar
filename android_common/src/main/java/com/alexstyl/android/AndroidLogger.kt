package com.alexstyl.android

import android.util.Log
import com.alexstyl.Logger

class AndroidLogger : Logger {
    override fun debug(message: String) {
        Log.d(this.javaClass.simpleName, message)
    }

    override fun warning(message: String) {
        Log.w(this.javaClass.simpleName, message)
    }
}
