package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import java.util.ArrayList
import java.util.Collections

class NamesInADate(val date: Date, private val names: MutableList<String> = ArrayList()) {

    fun getNames(): List<String> {
        return Collections.unmodifiableList(names)
    }

    @Deprecated("Do not use this method. Prefer passing names from the constructor of the class")
    fun addName(name: String) {
        names.add(name)
    }

    fun nameCount(): Int {
        return names.size
    }

}
