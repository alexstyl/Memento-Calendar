package com.alexstyl.specialdates.upcoming

import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.novoda.notils.caster.Views
import javax.inject.Inject

class UpcomingEventsFragment : MementoFragment(), UpcomingListMVPView, UpcomingEventsView {

    private lateinit var root: ViewGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private lateinit var upcomingList: RecyclerView

    private lateinit var adapter: UpcomingEventsAdapter
    private lateinit var askForSupport: AskForSupport

    lateinit var navigator: HomeNavigator
        @Inject set
    lateinit var imageLoader: ImageLoader
        @Inject set
    lateinit var refresher: UpcomingEventsViewRefresher
        @Inject set
    lateinit var presenter: UpcomingEventsPresenter
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationModule = (activity!!.application as MementoApplication).applicationModule
        applicationModule.inject(this)

        askForSupport = AskForSupport(activity)
        refresher.addEventsView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_events, container, false)
        root = Views.findById(view, R.id.root)
        progressBar = Views.findById(view, R.id.upcoming_events_progress)
        emptyView = Views.findById(view, R.id.upcoming_events_emptyview)

        upcomingList = Views.findById(view, R.id.upcoming_events_list)
        upcomingList.setHasFixedSize(true)
        upcomingList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        upcomingList.addItemDecoration(
                UpcomingEventsDecorator(
                        resources.getDimensionPixelSize(R.dimen.upcoming_event_header_vertical_spacing),
                        resources.getDimensionPixelSize(R.dimen.upcoming_event_vertical_spacing)
                ))

        adapter = UpcomingEventsAdapter(
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
        return view
    }

    override fun onResume() {
        super.onResume()
        refresher.addEventsView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresentingInto(this)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        upcomingList.visibility = View.GONE
        emptyView.visibility = View.GONE
    }

    override fun display(events: List<UpcomingRowViewModel>) {
        TransitionManager.beginDelayedTransition(root)

        progressBar.visibility = View.GONE
        adapter.displayUpcomingEvents(events)

        if (events.size > 0) {
            upcomingList.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        } else {
            upcomingList.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        }

        if (askForSupport.shouldAskForRating()) {
            askForSupport.askForRatingFromUser(activity)
        }
    }

    override val isShowingNoEvents: Boolean = upcomingList.childCount == 0

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
