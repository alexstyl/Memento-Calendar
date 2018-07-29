package com.alexstyl.specialdates.upcoming

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.UpcomingEventsView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.alexstyl.specialdates.home.HomeNavigator
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.support.AskForSupport
import com.alexstyl.specialdates.ui.base.MementoFragment
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener
import javax.inject.Inject

class UpcomingEventsFragment : MementoFragment(), UpcomingEventsView {

    @Inject lateinit var navigator: HomeNavigator
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var refresher: UpcomingEventsViewRefresher
    @Inject lateinit var presenter: UpcomingEventsPresenter
    @Inject lateinit var askForSupport: AskForSupport

    lateinit var mvpView: UpcomingListMVPView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationModule = (activity!!.application as MementoApplication).applicationModule
        applicationModule.inject(this)
        refresher.addEventsView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_events, container, false)
        val root = view.findViewById<FrameLayout>(R.id.root)
        val progressBar = view.findViewById<ProgressBar>(R.id.upcoming_events_progress)
        val emptyView = view.findViewById<TextView>(R.id.upcoming_events_emptyview)

        val upcomingList = view.findViewById<RecyclerView>(R.id.upcoming_events_list)
        upcomingList.setHasFixedSize(true)
        upcomingList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        upcomingList.addItemDecoration(
                UpcomingEventsDecorator(
                        resources.getDimensionPixelSize(R.dimen.upcoming_event_header_vertical_spacing),
                        resources.getDimensionPixelSize(R.dimen.upcoming_event_vertical_spacing)
                ))

        val adapter = UpcomingEventsAdapter(
                UpcomingViewHolderFactory(inflater, imageLoader),
                object : OnUpcomingEventClickedListener {

                    override fun onContactClicked(contact: Contact) {
                        navigator.toContactDetails(contact, activity!!)
                    }

                    override fun onNamedayClicked(date: Date) {
                        navigator.toDateDetails(date, activity!!)
                    }
                }
        )
        adapter.setHasStableIds(true)
        upcomingList.adapter = adapter
        mvpView = AndroidUpcomingMVPView(upcomingList, root, progressBar, emptyView, adapter, askForSupport, activity!!)

        return view
    }

    override fun onResume() {
        super.onResume()
        refresher.addEventsView(this)
    }

    override fun onStart() {
        super.onStart()
        startTracking()
        presenter.startPresentingInto(mvpView)
    }

    private fun startTracking() {
        Log.d("TIME", "Start loading at " + System.currentTimeMillis())
//        Debug.startMethodTracing()
    }

    override fun onPause() {
        super.onPause()
        refresher.removeView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.stopPresenting()
    }

    override fun onDestroy() {
        super.onDestroy()
        refresher.removeView(this)
    }

    override fun reloadUpcomingEventsView() {
        presenter.refreshEvents()
    }
}
