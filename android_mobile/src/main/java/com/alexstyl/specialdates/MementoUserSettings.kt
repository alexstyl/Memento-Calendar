package com.alexstyl.specialdates

interface MementoUserSettings {
    fun isFirstTimeBooting(): Boolean
    fun setFirstTimeBoot(firstBoot: Boolean)

}