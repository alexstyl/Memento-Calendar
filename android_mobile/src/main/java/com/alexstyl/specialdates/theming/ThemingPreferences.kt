package com.alexstyl.specialdates.theming

import android.content.Context

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R

class ThemingPreferences(private val preferences: EasyPreferences) {

    var selectedTheme: MementoTheme
        get() {
            val themeId = preferences.getInt(R.string.key_app_theme_id, DEFAULT_THEME.id)
            return MementoTheme.fromId(themeId)
        }
        set(selectedTheme) = preferences.setInteger(R.string.key_app_theme_id, selectedTheme.id)

    companion object {
        private val DEFAULT_THEME = MementoTheme.CHERRY_RED
    }
}
