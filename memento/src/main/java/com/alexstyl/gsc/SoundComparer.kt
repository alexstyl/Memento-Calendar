package com.alexstyl.gsc

class SoundComparer {
    companion object {
        private val soundRules = SoundRules.INSTANCE

        fun soundTheSame(first: String, second: String) = soundRules.compare(first, second, false)

        fun startsWith(fullWord: String, startsWith: String) = soundRules.compare(fullWord, startsWith, true)
    }
}
