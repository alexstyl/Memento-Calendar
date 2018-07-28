package com.alexstyl.specialdates.people

import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

class AndroidPeopleView(private val adapter: PeopleAdapter,
                        private val recyclerView: RecyclerView,
                        private val loadingView: ProgressBar) : PeopleView {


    override fun displayPeople(viewModels: List<PeopleRowViewModel>) {
        TransitionManager.beginDelayedTransition(recyclerView.parent as ViewGroup)
        adapter.updateWith(viewModels)
        loadingView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showLoading() {
        if (isDisplayNoData) {
            TransitionManager.beginDelayedTransition(recyclerView.parent as ViewGroup)
            recyclerView.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
        }
    }

    private val isDisplayNoData: Boolean
        get() = recyclerView.childCount == 0

}
