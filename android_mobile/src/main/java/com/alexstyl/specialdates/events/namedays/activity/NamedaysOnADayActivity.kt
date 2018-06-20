package com.alexstyl.specialdates.events.namedays.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Screen
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.getDateExtraOrThrow
import com.alexstyl.specialdates.date.putExtraDate
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import javax.inject.Inject


class NamedaysOnADayActivity : ThemedMementoActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var presenter: NamedaysInADayPresenter
    @Inject
    lateinit var dateLabelCreator: DateLabelCreator
    @Inject
    lateinit var analytics: Analytics

    private var dateView: TextView? = null
    private var namedaysOnADayNavigator: NamedaysOnADayNavigator? = null


    private lateinit var view: NamedaysOnADayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namedays)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        analytics.trackScreen(Screen.NAMEDAYS)
        namedaysOnADayNavigator = NamedaysOnADayNavigator(this, analytics)

        val toolbar = findViewById<MementoToolbar>(R.id.memento_toolbar)
        toolbar.displayNavigationIconAsUp()
        setSupportActionBar(toolbar)

        dateView = findViewById(R.id.namedays_date)

        val recyclerView = findViewById<RecyclerView>(R.id.namedays_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        val date = intent.getDateExtraOrThrow()
        dateView?.text = dateLabelCreator.createWithYearPreferred(date)

        val layoutInflater = LayoutInflater.from(this)
        val adapter = NamedaysScreenAdapter(
                NamedaysScreenViewHolderFactory(layoutInflater, imageLoader),
                { contact -> namedaysOnADayNavigator?.toContactDetails(contact) }
        )
        recyclerView.adapter = adapter
        view = AndroidNamedaysOnADayView(adapter)
    }

    override fun onStart() {
        super.onStart()
        val date = intent.getDateExtraOrThrow()
        presenter.startPresenting(view, date)
    }

    override fun onStop() {
        super.onStop()
        presenter.stopPresenting()
    }

    companion object {
        fun getStartIntent(context: Context, date: Date): Intent {
            return Intent(context, NamedaysOnADayActivity::class.java)
                    .putExtraDate(date)
        }
    }
}

