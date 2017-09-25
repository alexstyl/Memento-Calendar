package com.alexstyl.specialdates.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.widget.ColorImageView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.NativeExpressAdView
import com.novoda.notils.exception.DeveloperError

internal class UpcomingViewHolderFactory(private val layoutInflater: LayoutInflater, private val imageLoader: ImageLoader) {

    fun createFor(@UpcomingRowViewType viewType: Int, parent: ViewGroup): UpcomingRowViewHolder<*> {
        when (viewType) {
            UpcomingRowViewType.DATE_HEADER -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_date_header, parent, false)
                val dateView = view.findViewById(R.id.upcoming_event_date_header) as TextView
                return DateHeaderViewHolder(view, dateView)
            }
            UpcomingRowViewType.BANKHOLIDAY -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_bankholiday, parent, false)
                val holidayName = view.findViewById(R.id.row_upcoming_bankholiday) as TextView
                return BankholidayViewHolder(view, holidayName)
            }
            UpcomingRowViewType.NAMEDAY_CARD -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_nameday, parent, false)
                val namedays = view.findViewById(R.id.row_upcoming_namedays) as TextView
                return NamedaysViewHolder(view, namedays)
            }
            UpcomingRowViewType.CONTACT_EVENT -> {
                val view = layoutInflater.inflate(R.layout.row_upcoming_events_contact_event, parent, false)
                val avatarView = view.findViewById(R.id.row_upcoming_event_contact_image) as ColorImageView
                val contactName = view.findViewById(R.id.row_upcoming_event_contact_name) as TextView
                val eventLabel = view.findViewById(R.id.row_upcoming_event_contact_event) as TextView
                return ContactEventViewHolder(view, avatarView, contactName, eventLabel, imageLoader)
            }
            UpcomingRowViewType.AD -> {
                val adView = NativeExpressAdView(parent.context)
                adView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                adView.adSize = AdSize(AdSize.FULL_WIDTH, 132)
                adView.adUnitId = parent.resources.getString(R.string.admob_unit_id)
                return AdViewHolder(adView, adView)
            }
            else -> {
                throw DeveloperError("Unhandled viewType [$viewType]")
            }
        }
    }
}
