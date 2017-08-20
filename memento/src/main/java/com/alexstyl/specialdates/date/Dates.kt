package com.alexstyl.specialdates.date

import java.util.*

data class Dates(private val dates: ArrayList<Date>) {


    constructor() : this(ArrayList<Date>())

    constructor(date: Date) : this(ArrayList<Date>().apply {
        add(date)
    })

    constructor(dates: Dates) : this(ArrayList<Date>().apply {
        addAll(dates.dates)
    })

    fun getDate(i: Int): Date = dates[i]

    fun size(): Int = dates.size

    fun containsNoDate(): Boolean = dates.isEmpty()

    fun add(date: Date) {
        if (dates.contains(date)) {
            return
        }
        this.dates.add(date)
    }

    fun addAll(dates: Dates) {
        this.dates.addAll(dates.dates)
    }
}
