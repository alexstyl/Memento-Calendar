package com.alexstyl.specialdates.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView
import com.novoda.notils.exception.DeveloperError

internal class UpcomingViewHolderFactory(private val layoutInflater: LayoutInflater, private val imageLoader: ImageLoader) {

    fun createFor(@UpcomingRowViewType viewType: Int, parent: ViewGroup): UpcomingRowViewHolder<*> {
        when (viewType) {
            UpcomingRowViewType.DATE_HEADER -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_date_header, parent, false)
                val dateView = view.findViewById<TextView>(R.id.upcoming_event_date_header)
                return DateHeaderViewHolder(view, dateView)
            }
            UpcomingRowViewType.BANKHOLIDAY -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_bankholiday, parent, false)
                val holidayName = view.findViewById<TextView>(R.id.row_upcoming_bankholiday)
                return BankholidayViewHolder(view, holidayName)
            }
            UpcomingRowViewType.NAMEDAY_CARD -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_nameday, parent, false)
                val namedays = view.findViewById<TextView>(R.id.row_upcoming_namedays)
                return NamedaysViewHolder(view, namedays)
            }
            UpcomingRowViewType.CONTACT_EVENT -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_contact_event, parent, false)
                val avatarView = view.findViewById<ColorImageView>(R.id.row_upcoming_event_contact_image)
                val contactName = view.findViewById<TextView>(R.id.row_upcoming_event_contact_name)
                val eventLabel = view.findViewById<TextView>(R.id.row_upcoming_event_contact_event)
                return ContactEventViewHolder(view, avatarView, contactName, eventLabel, imageLoader)
            }
            else -> {
                throw DeveloperError("Unhandled viewType [$viewType]")
            }
        }
    }
}
