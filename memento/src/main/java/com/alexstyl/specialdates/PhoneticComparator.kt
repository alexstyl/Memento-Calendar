package com.alexstyl.specialdates

import com.alexstyl.gsc.SoundComparer

class PhoneticComparator : NameComparator {

    override fun compare(aWord: String, anOtherWord: String): Boolean {
        return SoundComparer.soundTheSame(aWord, anOtherWord)
    }

    override fun startsWith(aWord: String, anOtherWord: String): Boolean {
        return SoundComparer.startsWith(aWord, anOtherWord)
    }
}
