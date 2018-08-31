package com.alexstyl.specialdates.theming

import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import com.alexstyl.specialdates.R

enum class MementoTheme(val id: Int, @param:StringRes @field:StringRes
@get:StringRes
val themeName: Int, @param:StyleRes @field:StyleRes private val styleResId: Int) {
    MEMENTO(0, R.string.theme_Memento, R.style.Theme_Memento_Modern),
    NAVY_BLUE(1, R.string.theme_NavyBlue, R.style.Theme_Memento_NavyBlue),
    GLOSS_PINK(2, R.string.theme_GlossPink, R.style.Theme_Memento_GlossPink),
    EGGPLANT_GREEN(3, R.string.theme_Eggplant, R.style.Theme_Memento_Eggplant),
    MONOCHROME(4, R.string.theme_Monochrome, R.style.Theme_Memento_Monochrome),
    SYSTEMO(5, R.string.theme_Systemo, R.style.Theme_Memento_Systemo),
    AMBER(6, R.string.theme_Amber, R.style.Theme_Memento_Amber);

    @StyleRes
    fun androidTheme(): Int {
        return styleResId
    }

    companion object {

        fun fromId(themeId: Int): MementoTheme {
            return values()
                    .find { it.id == themeId }
                    ?: throw IllegalArgumentException("No theme exists with id $themeId")
        }
    }
}
