package com.alexstyl.specialdates.events.namedays.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.novoda.notils.caster.Views
import com.novoda.notils.logger.simple.Log

class NamedayActivity : ThemedMementoActivity(), NamedaysMVPView {

    private var presenter: NamedayPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namedays)

        val toolbar = Views.findById<MementoToolbar>(this, R.id.toolbar)
        setSupportActionBar(toolbar)

        presenter = NamedayPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter?.startPresentingInto(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.stopPresenting()
    }

    override fun displayNamedays(viewModels: List<NamedaysViewModel>) {
        viewModels.forEach {
            Log.d(it)
        }
    }

    companion object {
        fun getStartIntent(context: Context, dateSelected: Date): Intent {
            return Intent(context, NamedayActivity::class.java)
        }
    }
}
