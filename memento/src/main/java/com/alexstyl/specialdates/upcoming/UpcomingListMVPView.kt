package com.alexstyl.specialdates.upcoming

interface UpcomingListMVPView {

    val isShowingNoEvents: Boolean
    fun showLoading()
    fun display(events: List<UpcomingRowViewModel>)
}
