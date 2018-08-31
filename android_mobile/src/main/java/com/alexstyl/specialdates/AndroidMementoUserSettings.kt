package com.alexstyl.specialdates

import android.content.Context

class AndroidMementoUserSettings(private val easyPreferences: EasyPreferences) : MementoUserSettings {

    override fun isFirstTimeBooting() = easyPreferences.getBoolean(KEY_FIRST_BOOT, true)

    override fun setFirstTimeBoot(firstBoot: Boolean) {
        easyPreferences.setBoolean(KEY_FIRST_BOOT, firstBoot)
    }


    companion object {
        private const val KEY_FIRST_BOOT = "key:first_boot"
        private const val FILE_MEMENTO_SETTINGS = "file_memento_settings"

        fun create(context: Context): MementoUserSettings {
            return AndroidMementoUserSettings(EasyPreferences.createForPrivatePreferences(context, FILE_MEMENTO_SETTINGS))
        }
    }
}