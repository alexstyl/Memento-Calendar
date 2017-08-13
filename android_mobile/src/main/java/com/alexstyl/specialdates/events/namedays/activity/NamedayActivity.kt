package com.alexstyl.specialdates.events.namedays.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateBundleUtils
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import javax.inject.Inject

class NamedayActivity : ThemedMementoActivity(), NamedaysMVPView {

    @Inject lateinit var namedayCalendar: NamedayCalendar
    @Inject lateinit var namedaysViewModelFactory: NamedaysViewModelFactory
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var presenter: NamedayPresenter

    private var screenAdapter: NamedaysScreenAdapter? = null
    private val namedayNavigator = NamedayNavigator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namedays)

        (application as MementoApplication).applicationModule.inject(this)


        val recyclerView = findViewById(R.id.namedays_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val namedaysAdapter = NamedaysScreenAdapter(NamedaysScreenViewHolderFactory(LayoutInflater.from(this), imageLoader), {
            contact ->
            namedayNavigator.toContactDetails(contact)
        })
        screenAdapter = namedaysAdapter
        recyclerView.adapter = screenAdapter
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

    companion object {
        fun getStartIntent(context: Context, dateSelected: Date): Intent {
            val intent = Intent(context, NamedayActivity::class.java)
            DateBundleUtils.putDateAsExtraIntoIntent(dateSelected, intent)
            return intent
        }
    }
}
