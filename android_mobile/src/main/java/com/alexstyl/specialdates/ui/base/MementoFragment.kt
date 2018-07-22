package com.alexstyl.specialdates.ui.base

import android.app.Activity
import android.content.ContentResolver
import android.support.v4.app.Fragment

open class MementoFragment : Fragment() {


    override fun onAttach(activity: Activity?) {
        if (activity !is MementoActivity) {
            throw IllegalStateException(MementoFragment::class.java.simpleName + " must be attached to a BaseActivity.")
        }
        mementoActivity = activity

        super.onAttach(activity)
    }

    var mementoActivity: MementoActivity? = null

    protected val contentResolver: ContentResolver
        get() = activity!!.contentResolver

}
