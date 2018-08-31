package com.alexstyl.specialdates.facebook.friendimport

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.events.namedays.NamedayLocale

class SystemLogTracker : CrashAndErrorTracker {

    override fun track(e: Throwable) {
        e.printStackTrace()
    }

    override fun log(message: String) {
        println(message)
    }

    override fun startTracking() {
        // do nothing
    }

    override fun onNamedayLocaleChanged(locale: NamedayLocale?) {
        // do nothing
    }

    override fun updateLocaleUsed() {
        // do nothing

    }
}
