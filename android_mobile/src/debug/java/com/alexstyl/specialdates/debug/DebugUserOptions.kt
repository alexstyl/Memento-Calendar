package com.alexstyl.specialdates.debug

import android.content.Context
import android.support.annotation.StringRes

import com.alexstyl.specialdates.EasyPreferences

class DebugUserOptions private constructor(private val preferences: EasyPreferences) {

    fun wipe() {
        preferences.clear()
    }

    companion object {
        fun newInstance(context: Context, @StringRes prefKey: Int): DebugUserOptions {
            return DebugUserOptions(EasyPreferences.createForPrivatePreferences(context, prefKey))
        }
    }
}
