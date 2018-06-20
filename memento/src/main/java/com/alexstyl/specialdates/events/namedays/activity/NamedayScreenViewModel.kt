package com.alexstyl.specialdates.events.namedays.activity


interface NamedayScreenViewModel {
    @get:NamedayScreenViewType
    val viewType: Int
    val id: Long
}

