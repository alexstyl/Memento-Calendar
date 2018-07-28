package com.alexstyl.specialdates.people

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.UpcomingEventsView
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.home.HomeNavigator
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.MementoFragment
import javax.inject.Inject

open class PeopleFragment : MementoFragment(), UpcomingEventsView {

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var navigator: HomeNavigator
    @Inject lateinit var presenter: PeoplePresenter
    @Inject lateinit var refresher: UpcomingEventsViewRefresher

    lateinit var mvpView: PeopleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MementoApplication).applicationModule.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_people, container, false)


        val adapter = PeopleAdapter(imageLoader, inflater, object : PeopleViewHolderListener {
            override fun onPersonClicked(contact: Contact) {
                navigator.toContactDetails(contact, activity!!)
            }

            override fun onFacebookImport() {
                navigator.toFacebookImport(activity!!)
            }

            override fun onAddContactClicked() {
                navigator.toAddEvent(activity!!, HomeActivity.CODE_ADD_EVENT)
            }
        })
        val recyclerView = inflate.findViewById(R.id.people_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(PeopleItemDecorator(resources.getDimensionPixelSize(R.dimen.people_import_bottom_spacing), resources.getDimensionPixelSize(R.dimen.people_inbetween_spacing)))

        val loadingView = inflate.findViewById(R.id.people_loading) as ProgressBar
        mvpView = AndroidPeopleView(adapter, recyclerView, loadingView)
        return inflate
    }

    override fun onResume() {
        super.onResume()
        presenter.startPresentingInto(mvpView)
        refresher.addEventsView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.stopPresenting()
        refresher.removeView(this)
    }

    override fun reloadUpcomingEventsView() {
        presenter.refreshData()
    }
}
