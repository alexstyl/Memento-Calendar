package com.alexstyl.specialdates.events.namedays.activity

data class NamedaysViewModel(val name: String)
    : NamedayScreenViewModel {

    override val viewType: Int
        get() = NamedayScreenViewType.NAMEDAY
    override val id: Long
        get() = hashCode().toLong()


}

