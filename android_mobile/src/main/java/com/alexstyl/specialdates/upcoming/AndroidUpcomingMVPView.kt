package com.alexstyl.specialdates.upcoming

import android.app.Activity
import android.os.Debug
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.alexstyl.specialdates.support.AskForSupport

class AndroidUpcomingMVPView(
        private val upcomingList: RecyclerView,
        private val root: ViewGroup,
        private val progressBar: ProgressBar,
        private val emptyView: TextView,
        private val adapter: UpcomingEventsAdapter,
        private val askForSupport: AskForSupport,
        private val activity: Activity

) : UpcomingListMVPView {
    override val isShowingNoEvents: Boolean
        get() = upcomingList.childCount == 0

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        upcomingList.visibility = View.GONE
        emptyView.visibility = View.GONE
    }

    override fun display(events: List<UpcomingRowViewModel>) {
        stopTracking()
        TransitionManager.beginDelayedTransition(root)

        progressBar.visibility = View.GONE
        adapter.displayUpcomingEvents(events)

        if (events.isNotEmpty()) {
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

    private fun stopTracking() {
        Log.d("TIME", "Done loading at " + System.currentTimeMillis())
        Debug.stopMethodTracing()
    }

}
