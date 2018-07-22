package com.alexstyl.specialdates.debug.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexstyl.specialdates.debug.DebugOption
import com.alexstyl.specialdates.debug.MementoDebugFragment

class DebugContactsFragment : MementoDebugFragment() {
    override val debugOption: DebugOption
        get() = DebugOption.CONTACTS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
    }

}
