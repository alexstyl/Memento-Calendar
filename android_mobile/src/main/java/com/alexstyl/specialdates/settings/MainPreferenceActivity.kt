package com.alexstyl.specialdates.settings

import android.os.Bundle
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity
import com.alexstyl.specialdates.ui.widget.MementoToolbar
import com.novoda.notils.caster.Views

class MainPreferenceActivity : MementoPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = Views.findById<MementoToolbar>(this, R.id.memento_toolbar)
        setSupportActionBar(toolbar)
        toolbar.displayNavigationIconAsUp()
    }

}
