package com.alexstyl.specialdates.events.namedays.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alexstyl.specialdates.MementoApplication

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateBundleUtils
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.novoda.notils.caster.Views
import com.novoda.notils.logger.simple.Log
import javax.inject.Inject

class NamedayActivity : ThemedMementoActivity(), NamedaysMVPView {

    @Inject lateinit var namedayCalendar: NamedayCalendar
    @Inject lateinit var namedaysViewModelFactory: NamedaysViewModelFactory

    private var presenter: NamedayPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namedays)

        (application as MementoApplication).applicationModule.inject(this)

        val toolbar = Views.findById<MementoToolbar>(this, R.id.toolbar)
        setSupportActionBar(toolbar)

        presenter = NamedayPresenter(namedayCalendar, namedaysViewModelFactory)
    }

    override fun onStart() {
        super.onStart()

        val date = DateBundleUtils.extractDateFrom(intent)
        presenter?.startPresenting(into = this, forDate = date)
    }

    override fun onStop() {
        super.onStop()
        presenter?.stopPresenting()
    }

    override fun displayNamedays(viewModels: List<NamedaysViewModel>) {
        // TODO bind to the UI
        Log.d("displayNamedays called " + viewModels)
    }

    companion object {
        fun getStartIntent(context: Context, dateSelected: Date): Intent {
            val intent = Intent(context, NamedayActivity::class.java)
            DateBundleUtils.putDateAsExtraIntoIntent(dateSelected, intent)
            return intent
        }
    }
}
