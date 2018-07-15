package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.content.res.Resources
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.upcoming.UpcomingEventsProvider
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType

class UpcomingEventsViewsFactory(private val packageName: String,
                                 private val upcomingEventsProvider: UpcomingEventsProvider,
                                 private val context: Context,
                                 private val resources: Resources,
                                 private val avatarFactory: CircularAvatarFactory)
    : RemoteViewsService.RemoteViewsFactory {

    private var rows: List<UpcomingRowViewModel>? = null

    override fun onCreate() {
        // do nothing
    }

    override fun onDataSetChanged() {
        val date = Date.today()
        rows = upcomingEventsProvider.calculateEventsBetween(
                TimePeriod.between(date, date.addDay(DAYS_IN_A_MONTH))
        )
    }

    override fun getViewAt(position: Int): RemoteViews {
        val viewModel = rows!![position]
        val binder = binderFor(viewModel)
        binder.bind(viewModel)
        return binder.views
    }

    private fun binderFor(viewModel: UpcomingRowViewModel): UpcomingEventViewBinder {
        return when (viewModel.viewType) {
            UpcomingRowViewType.DATE_HEADER -> {
                val view = RemoteViews(packageName, R.layout.widget_upcoming_events_list_date)
                DateHeaderBinder(view)
            }
            UpcomingRowViewType.BANKHOLIDAY -> {
                val view = RemoteViews(packageName, R.layout.widget_upcomingevents_list_bankholiday)
                BankHolidayBinder(view, context)
            }
            UpcomingRowViewType.NAMEDAY_CARD -> {
                val view = RemoteViews(packageName, R.layout.widget_upcoming_events_list_nameday)
                NamedaysBinder(view, context)
            }
            UpcomingRowViewType.CONTACT_EVENT -> {
                val remoteViews = RemoteViews(packageName, R.layout.widget_upcoming_events_list_contact_event)
                ContactEventBinder(remoteViews, resources, context, avatarFactory)
            }
            else -> throw IllegalStateException("Unhandled type " + viewModel.viewType)
        }
    }


    override fun getLoadingView(): RemoteViews? {
        return null // use the default loading view by returning null
    }

    override fun getCount(): Int {
        return rows?.size ?: 0
    }

    override fun getViewTypeCount(): Int {
        return VIEW_TYPE_COUNT
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDestroy() {
        // do nothing
    }

    companion object {
        private const val VIEW_TYPE_COUNT = 3
        private const val DAYS_IN_A_MONTH = 30
    }
}
