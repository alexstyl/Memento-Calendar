package com.alexstyl.specialdates.debug

import android.app.Activity
import com.alexstyl.specialdates.ui.base.MementoFragment

abstract class MementoDebugFragment : MementoFragment() {

    private var parentActivity: DebugActivity? = null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.parentActivity = activity as DebugActivity
    }

    override fun onStart() {
        super.onStart()
        parentActivity?.title = debugOption.title
    }

    abstract val debugOption: DebugOption

}
