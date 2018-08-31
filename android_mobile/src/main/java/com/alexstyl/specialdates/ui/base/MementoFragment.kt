package com.alexstyl.specialdates.ui.base

import android.content.ContentResolver
import android.support.v4.app.Fragment

open class MementoFragment : Fragment() {

    protected val contentResolver: ContentResolver
        get() = activity!!.contentResolver

}
