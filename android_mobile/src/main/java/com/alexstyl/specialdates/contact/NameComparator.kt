package com.alexstyl.specialdates.contact

import com.alexstyl.gsc.SoundComparer

object NameComparator {

    fun areTheSameName(left: String, right: String): Boolean {
        try {
            return SoundComparer.areSame(left, right)
        } catch (e: Exception) {
            return false
        }

    }

}
