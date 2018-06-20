package com.alexstyl.specialdates.events.namedays.activity

class AndroidNamedaysOnADayView(private val screenAdapter: NamedaysScreenAdapter) : NamedaysOnADayView {
    override fun displayNamedays(viewModels: List<NamedayScreenViewModel>) {
        screenAdapter.display(viewModels)
    }
}
