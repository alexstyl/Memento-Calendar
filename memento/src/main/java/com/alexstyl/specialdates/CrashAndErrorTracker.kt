package com.alexstyl.specialdates

import com.alexstyl.specialdates.events.namedays.NamedayLocale

interface CrashAndErrorTracker {
    fun startTracking()

    fun track(e: Throwable)

    fun onNamedayLocaleChanged(locale: NamedayLocale)

    fun updateLocaleUsed()

    fun log(message: String)
}
