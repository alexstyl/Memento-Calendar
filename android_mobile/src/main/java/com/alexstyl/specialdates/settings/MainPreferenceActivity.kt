package com.alexstyl.specialdates.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity
import com.novoda.notils.caster.Views

class MainPreferenceActivity : MementoPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = Views.findById<MementoToolbar>(this, R.id.memento_toolbar)
        setSupportActionBar(toolbar)
        toolbar.displayNavigationIconAsUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val up = Intent(this, UpcomingEventsActivity::class.java)
                up.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(up)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
