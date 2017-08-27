package com.alexstyl.specialdates.events.namedays.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateBundleUtils
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import javax.inject.Inject

class NamedayActivity : ThemedMementoActivity(), NamedaysMVPView {

    @Inject lateinit var namedayCalendar: NamedayCalendar
    @Inject lateinit var namedaysViewModelFactory: NamedaysViewModelFactory
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var presenter: NamedayPresenter
    @Inject lateinit var dateLabelCreator: DateLabelCreator
    @Inject lateinit var analytics: Analytics

    private var screenAdapter: NamedaysScreenAdapter? = null
    private var dateView: TextView? = null
    private var namedayNavigator: NamedayNavigator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namedays)

        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)

        namedayNavigator = NamedayNavigator(this, analytics)

        val toolbar = findViewById(R.id.memento_toolbar) as MementoToolbar
        toolbar.displayNavigationIconAsUp()
        setSupportActionBar(toolbar)

        dateView = findViewById(R.id.namedays_date) as TextView

        val recyclerView = findViewById(R.id.namedays_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val namedaysAdapter = NamedaysScreenAdapter(NamedaysScreenViewHolderFactory(LayoutInflater.from(this), imageLoader),
                { contact ->
                    namedayNavigator?.toContactDetails(contact)
                })
        screenAdapter = namedaysAdapter
        recyclerView.adapter = screenAdapter

        val date = DateBundleUtils.extractDateFrom(intent)
        dateView?.text = dateLabelCreator.createLabelWithYearPreferredFor(date)
    }


    override fun onStart() {
        super.onStart()
        val date = DateBundleUtils.extractDateFrom(intent)
        presenter.startPresenting(into = this, forDate = date)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayNamedays(viewModels: List<NamedayScreenViewModel>) {
        screenAdapter?.display(viewModels)
    }

    override fun onStop() {
        super.onStop()
        presenter.stopPresenting()
    }

    override fun shouldUseHomeAsUp(): Boolean = true

    companion object {
        fun getStartIntent(context: Context, dateSelected: Date): Intent {
            val intent = Intent(context, NamedayActivity::class.java)
            DateBundleUtils.putDateAsExtraIntoIntent(dateSelected, intent)
            return intent
        }
    }
}

