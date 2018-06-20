package com.alexstyl.specialdates.upcoming

import android.support.annotation.ColorInt

data class DateHeaderViewModel(val date: String, @ColorInt val titleColor: Int) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.DATE_HEADER

    override val id: Long
        get() = date.hashCode().toLong()

}
