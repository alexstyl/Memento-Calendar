package com.alexstyl.specialdates.upcoming

interface UpcomingRowViewModel {

    @get:UpcomingRowViewType
    val viewType: Int

    val id: Long
}
