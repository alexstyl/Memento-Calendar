package com.alexstyl.specialdates

interface NameComparator {

    fun compare(aWord: String, anOtherWord: String): Boolean
    fun startsWith(aWord: String, anOtherWord: String): Boolean

}
