package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

data class ArrayNamesInADate(override val date: Date, override val names: MutableList<String>)
    : MutableNamesInADate {

    override fun addName(name: String) {
        names.add(name)
    }

}
