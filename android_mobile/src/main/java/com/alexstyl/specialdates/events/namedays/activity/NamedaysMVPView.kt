package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.date.Date

interface NamedaysMVPView {
    fun displayNamedays(date: Date, viewModels: List<NamedayScreenViewModel>)
}
